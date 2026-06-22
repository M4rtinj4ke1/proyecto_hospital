# Sistema de Gestión Hospitalaria - Microservicios

## Contexto

Sistema distribuido de gestión hospitalaria desarrollado con arquitectura de microservicios usando Spring Boot 4.0.6, Spring Cloud 2025.1.1 y Docker. Permite gestionar pacientes, médicos, citas, especialidades, historiales clínicos, recetas, pagos y notificaciones a través de servicios independientes comunicados mediante REST y Feign Client, con descubrimiento de servicios vía Eureka, configuración centralizada vía Spring Cloud Config Server y enrutamiento unificado vía API Gateway.

---

## Integrantes

| Nombre | Rol |
|--------|-----|
| Martín Jaque | Desarrollador Full Stack |

---

## Arquitectura

```
                          ┌──────────────────┐
                          │   API Gateway    │  :8080
                          └────────┬─────────┘
                                   │
              ┌────────────────────┼────────────────────┐
              │                    │                     │
       ┌──────▼──────┐     ┌───────▼──────┐      ┌───────▼───────┐
       │ Eureka      │     │ Config       │      │  Microservicios │
       │ Server      │     │ Server       │      │  de negocio     │
       │ :8761       │     │ :8888        │      │  (8 servicios)  │
       └─────────────┘     └──────────────┘      └────────┬───────┘
                                                            │
                                                   ┌────────▼────────┐
                                                   │  MySQL :3307    │
                                                   │  1 BD por       │
                                                   │  microservicio  │
                                                   └─────────────────┘
```

---

## Microservicios implementados

| Servicio | Descripción | Base de datos | Depende de Feign |
|----------|-------------|----------------|-------------------|
| `eureka-server` | Registro y descubrimiento de servicios | — | — |
| `config-server` | Configuración centralizada | — | — |
| `api-gateway` | Punto de entrada único, enrutamiento centralizado | — | — |
| `pacientes-service` | Gestión de pacientes del sistema hospitalario | `bd_pacientes` | — |
| `medicos-service` | Gestión de médicos y sus especialidades | `bd_medicos` | especialidades-service |
| `citas-service` | Gestión de citas médicas | `bd_citas` | pacientes-service |
| `especialidades-service` | Gestión de especialidades médicas | `bd_especialidades` | — |
| `historial-service` | Historial clínico de pacientes | `bd_historial` | pacientes-service, medicos-service |
| `recetas-service` | Gestión de recetas médicas | `bd_recetas` | citas-service |
| `pagos-service` | Gestión de pagos de atenciones | `bd_pagos` | citas-service |
| `notificaciones-service` | Gestión de notificaciones del sistema | `bd_notificaciones` | — |

Todos los microservicios de negocio corren en puerto dinámico (`server.port: 0`) y se registran en Eureka usando su `spring.application.name`. El API Gateway los expone hacia el exterior en el puerto `8080`.

---

## Rutas principales del API Gateway

| Método | Ruta | Servicio destino |
|--------|------|-------------------|
| GET/POST/PUT/DELETE | `/api/pacientes/**` | pacientes-service |
| GET/POST/PUT/DELETE | `/api/medicos/**` | medicos-service |
| GET/POST/PUT/DELETE | `/api/citas/**` | citas-service |
| GET/POST/PUT/DELETE | `/api/especialidades/**` | especialidades-service |
| GET/POST/PUT/DELETE | `/api/historiales/**` | historial-service |
| GET/POST/PUT/DELETE | `/api/recetas/**` | recetas-service |
| GET/POST/PUT/DELETE | `/api/pagos/**` | pagos-service |
| GET/POST/PUT/DELETE | `/api/notificaciones/**` | notificaciones-service |

---

## Documentación Swagger

Cada microservicio expone su propia documentación OpenAPI de forma individual:

```
http://localhost:8080/api/pacientes
http://localhost:8080/api/medicos
http://localhost:8080/api/citas
http://localhost:8080/api/especialidades
http://localhost:8080/api/historial
http://localhost:8080/api/recetas
http://localhost:8080/api/pagos
http://localhost:8080/api/notificaciones
```

El API Gateway centraliza el acceso a Swagger UI en:

```
http://localhost:8080/swagger-ui.html
```

> **Nota:** si la interfaz de Swagger UI agregada no carga los servicios correctamente, también es posible acceder a la documentación de cada microservicio de forma directa contra su propio contenedor (sin pasar por el gateway), usando `docker compose exec` o exponiendo temporalmente su puerto interno para depuración.

---

## Requisitos previos

- Docker Desktop instalado y en ejecución
- Puertos `8080`, `8761`, `8888` y `3307` disponibles en el equipo host
- JDK 25 instalado localmente **solo si se va a compilar o testear fuera de Docker** (Docker construye su propia imagen con `maven:3.9.11-eclipse-temurin-25` y `eclipse-temurin:25-jre`, por lo que no es estrictamente necesario tener el JDK en el host para levantar el proyecto vía Docker)

---

## Guía de despliegue

### Despliegue con Docker (recomendado)


# Construir y levantar todos los servicios
docker compose build
docker compose up
```

El `docker-compose.yml` orquesta el orden de arranque mediante `depends_on` y `healthcheck`:

1. `mysql-db` levanta primero y espera a pasar su healthcheck antes de permitir que los microservicios se conecten.
2. `eureka` y `config-server` levantan después.
3. Los 8 microservicios de negocio levantan una vez que MySQL está saludable y Eureka/Config Server están iniciados.
4. `api-gateway` levanta al final, después de todos los microservicios de negocio.

### Verificar que todo esté funcionando

1. **Eureka Dashboard** — confirma que todos los servicios estén registrados:
   ```
   http://localhost:8761
   ```

2. **API Gateway** — punto de entrada único hacia todos los microservicios:
   ```
   http://localhost:8080
   ```

3. **MySQL** — accesible desde el host en el puerto `3307` (mapeado desde el `3306` interno del contenedor):
   ```
   host: localhost
   puerto: 3307
   usuario: root
   password: root
   ```

### Detener el entorno

```bash
docker compose down
```

Para eliminar también los datos persistidos de MySQL:

```bash
docker compose down -v
```

---

## Desarrollo local (sin Docker)

Si se necesita ejecutar un microservicio individual fuera de Docker (por ejemplo, para depurar o correr tests desde el IDE):

### Requisitos
- JDK 25 (ej. [Eclipse Temurin](https://adoptium.net/temurin/releases/?version=25))
- El proyecto incluye Maven Wrapper en cada módulo — no es necesario instalar Maven por separado

### Compilar y correr tests

```bash
cd <nombre-del-microservicio>
./mvnw clean install        # Linux / macOS
.\mvnw.cmd clean install    # Windows
```

### Variables de entorno necesarias

Cada microservicio de negocio espera estas variables (normalmente provistas por `docker-compose.yml`, pero necesarias si se ejecuta el `.jar` de forma manual):

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/<nombre_bd>
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://localhost:8761/eureka/
SPRING_CONFIG_IMPORT=configserver:http://localhost:8888
```
### swagger url
http://localhost:8080/swagger-ui/index.html
http://localhost:8080/pacientes-docs/swagger-ui.html
---

## Pruebas

Los microservicios incluyen pruebas unitarias (capa Service, con Mockito) y pruebas de integración de la capa Controller (`@WebMvcTest`, con MockMvc).

```bash
cd <nombre-del-microservicio>
.\mvnw.cmd test
```

| Microservicio | Tests Service | Tests Controller |
|----------------|----------------|--------------------|
| pacientes-service | ✅ | ✅ |
| medicos-service | ✅ | ✅ |
| citas-service | — | — |
| historial-service | ✅ | ✅ |
| especialidades-service | — | — |
| pagos-service | — | — |
| recetas-service | — | — |
| notificaciones-service | — | — |

---

## Stack tecnológico

- **Java 25**
- **Spring Boot 4.0.6**
- **Spring Cloud 2025.1.1** (Eureka, Config Server, OpenFeign, Gateway)
- **MySQL 8.0**
- **Flyway** (migraciones de base de datos)
- **springdoc-openapi** (documentación Swagger/OpenAPI 3)
- **Lombok**
- **JUnit 5 + Mockito** (pruebas unitarias)
- **Docker / Docker Compose**
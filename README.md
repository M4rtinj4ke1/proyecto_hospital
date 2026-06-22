# Sistema de Gestión Hospitalaria - Microservicios

## Contexto

Sistema distribuido de gestión hospitalaria desarrollado con arquitectura de microservicios usando Spring Boot 4.0.6, Spring Cloud 2025.1.1 y Docker. Permite gestionar pacientes, médicos, citas, especialidades, historiales clínicos, recetas, pagos y notificaciones a través de servicios independientes comunicados mediante REST y Feign Client, con descubrimiento de servicios vía Eureka y configuración centralizada vía Spring Cloud Config Server.

---

## Integrantes

| Nombre | Rol |
|--------|-----|
| Martín Jaque | Desarrollador Full Stack |

---

## Microservicios implementados

| Servicio | Descripción | Puerto |
|----------|-------------|--------|
| `eureka-server` | Registro y descubrimiento de servicios | 8761 |
| `config-server` | Configuración centralizada (perfil nativo) | 8888 |
| `api-gateway` | Punto de entrada único, enrutamiento centralizado | 8080 |
| `pacientes-service` | Gestión de pacientes del sistema hospitalario | Dinámico |
| `medicos-service` | Gestión de médicos y sus especialidades | Dinámico |
| `citas-service` | Gestión de citas médicas | Dinámico |
| `especialidades-service` | Gestión de especialidades médicas | Dinámico |
| `historial-service` | Historial clínico de pacientes | Dinámico |
| `recetas-service` | Gestión de recetas médicas | Dinámico |
| `pagos-service` | Gestión de pagos de atenciones | Dinámico |
| `notificaciones-service` | Gestión de notificaciones del sistema | Dinámico |

---

## Rutas principales del API Gateway

| Método | Ruta | Servicio destino |
|--------|------|-----------------|
| GET/POST | `/api/pacientes/**` | pacientes-service |
| GET/POST | `/api/medicos/**` | medicos-service |
| GET/POST | `/api/citas/**` | citas-service |
| GET/POST | `/api/especialidades/**` | especialidades-service |
| GET/POST | `/api/historiales/**` | historial-service |
| GET/POST | `/api/recetas/**` | recetas-service |
| GET/POST | `/api/pagos/**` | pagos-service |
| GET/POST | `/api/notificaciones/**` | notificaciones-service |

---

## Documentación Swagger

La documentación está centralizada en el API Gateway:
http://localhost:8080/swagger-ui/index.html

Documentación individual por servicio (spec JSON):
http://localhost:8080/v3/api-docs/pacientes

http://localhost:8080/v3/api-docs/medicos
---

## Guía de despliegue

### Requisitos previos
- Docker Desktop instalado y corriendo
- Puerto 8080, 8761, 8888 y 3307 disponibles

### Despliegue con Docker (recomendado)

```bash
# Clonar el repositorio
git clone <url-del-repositorio>
cd proyecto_hospital

# Construir y levantar todos los servicios
docker-compose build
docker-compose up
```

Verificar que todos los servicios estén registrados en Eureka:
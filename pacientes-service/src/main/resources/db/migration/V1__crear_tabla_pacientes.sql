CREATE TABLE IF NOT EXISTS pacientes (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(30) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    email VARCHAR(255) NOT NULL
    );
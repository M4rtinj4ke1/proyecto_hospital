CREATE TABLE IF NOT EXISTS notificaciones (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              paciente_id BIGINT NOT NULL,
                                              mensaje VARCHAR(500) NOT NULL,
    fecha_envio DATETIME,
    tipo VARCHAR(50) NOT NULL
    );
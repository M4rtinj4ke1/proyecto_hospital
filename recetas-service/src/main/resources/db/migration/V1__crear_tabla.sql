CREATE TABLE IF NOT EXISTS recetas (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       cita_id BIGINT NOT NULL,
                                       medicamento VARCHAR(255) NOT NULL,
    posologia VARCHAR(255) NOT NULL,
    fecha_emision DATE NOT NULL
    );
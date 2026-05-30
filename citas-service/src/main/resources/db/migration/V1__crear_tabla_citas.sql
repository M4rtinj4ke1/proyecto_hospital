CREATE TABLE IF NOT EXISTS citas (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     fecha DATE NOT NULL,
                                     descripcion VARCHAR(255),
    paciente_id BIGINT NOT NULL
    );
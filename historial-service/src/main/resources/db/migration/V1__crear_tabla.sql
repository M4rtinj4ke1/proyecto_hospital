CREATE TABLE IF NOT EXISTS historial (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         paciente_id BIGINT NOT NULL,
                                         medico_id BIGINT NOT NULL,
                                         fecha DATE NOT NULL,
                                         diagnostico VARCHAR(255) NOT NULL,
    tratamiento VARCHAR(255)
    );
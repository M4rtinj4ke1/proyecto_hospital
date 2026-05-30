CREATE TABLE IF NOT EXISTS pagos (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     cita_id BIGINT NOT NULL,
                                     monto DOUBLE NOT NULL,
                                     metodo_pago VARCHAR(50) NOT NULL,
    estado VARCHAR(50) NOT NULL
    );
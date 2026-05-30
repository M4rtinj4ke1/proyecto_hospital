package cl.duoc.notificaciones_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity @Table(name = "notificaciones")
public class Notificacion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El paciente es obligatorio.")
    private Long pacienteId;

    @NotBlank(message = "El mensaje no puede estar vacío.")
    private String mensaje;

    private LocalDateTime fechaEnvio;

    @NotBlank(message = "El tipo no puede estar vacío.")
    private String tipo;
}
package cl.duoc.historial_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity
@Table(name = "historial")
public class Historial {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El paciente es obligatorio.")
    private Long pacienteId;

    @NotNull(message = "El médico es obligatorio.")
    private Long medicoId;

    @NotNull(message = "La fecha es obligatoria.")
    private LocalDate fecha;

    @NotBlank(message = "El diagnóstico no puede estar vacío.")
    private String diagnostico;

    private String tratamiento;
}
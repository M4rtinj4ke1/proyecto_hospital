package cl.duoc.historial_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class HistorialDTO {

    private Long id;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El diagnóstico no puede estar vacío")
    private String diagnostico;

    private String tratamiento;

    @NotNull(message = "El paciente es obligatorio")
    @Valid
    private PacienteDTO paciente;

    @NotNull(message = "El médico es obligatorio")
    @Valid
    private MedicoDTO medico;
}
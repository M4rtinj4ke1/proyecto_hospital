package cl.duoc.historial_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Data
public class HistorialDTO {
    private Long id;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private PacienteDTO paciente;
    private MedicoDTO medico;
}
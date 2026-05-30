package cl.duoc.pagos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Data
public class CitaDTO {
    private Long id;
    private LocalDate fecha;
    private String descripcion;
    private PacienteDTO paciente;
}

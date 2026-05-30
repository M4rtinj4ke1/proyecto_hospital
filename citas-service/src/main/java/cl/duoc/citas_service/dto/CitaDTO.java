package cl.duoc.citas_service.dto;
import lombok.Data;
import java.time.LocalDate;
@Data
public class CitaDTO {
    private Long id;
    private LocalDate fecha;
    private String descripcion;
    private PacienteDTO paciente;
}
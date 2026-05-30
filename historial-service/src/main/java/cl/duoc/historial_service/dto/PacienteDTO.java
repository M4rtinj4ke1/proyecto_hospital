package cl.duoc.historial_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PacienteDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String email;
}
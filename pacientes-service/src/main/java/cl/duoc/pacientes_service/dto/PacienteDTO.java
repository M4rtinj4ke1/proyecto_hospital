package cl.duoc.pacientes_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String email;
}
package cl.duoc.citas_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PacienteDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String email;
}

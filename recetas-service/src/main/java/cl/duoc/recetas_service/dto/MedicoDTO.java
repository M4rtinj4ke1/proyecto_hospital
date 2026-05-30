package cl.duoc.recetas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class MedicoDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String apellidos;
    private String email;
    private EspecialidadDTO especialidad;
}
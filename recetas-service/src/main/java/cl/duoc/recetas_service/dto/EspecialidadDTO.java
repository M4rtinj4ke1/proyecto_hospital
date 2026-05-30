package cl.duoc.recetas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class EspecialidadDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}
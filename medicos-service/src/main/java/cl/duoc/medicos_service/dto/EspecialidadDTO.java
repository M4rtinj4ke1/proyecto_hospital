package cl.duoc.medicos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EspecialidadDTO {
    private Long id;
    private String nombre;
    private String descripcion;
}

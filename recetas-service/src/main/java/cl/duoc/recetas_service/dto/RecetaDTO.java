package cl.duoc.recetas_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Data
public class RecetaDTO {
    private Long id;
    private String medicamento;
    private String posologia;
    private LocalDate fechaEmision;
    private CitaDTO cita;
}
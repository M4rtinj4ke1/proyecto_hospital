package cl.duoc.pagos_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
public class PagoDTO {
    private Long id;
    private Double monto;
    private String metodoPago;
    private String estado;
    private CitaDTO cita;
}
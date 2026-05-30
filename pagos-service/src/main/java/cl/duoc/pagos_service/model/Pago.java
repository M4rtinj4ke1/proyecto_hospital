package cl.duoc.pagos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity @Table(name = "pagos")
public class Pago {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cita es obligatoria.")
    private Long citaId;

    @NotNull(message = "El monto es obligatorio.")
    private Double monto;

    @NotBlank(message = "El método de pago no puede estar vacío.")
    private String metodoPago;

    @NotBlank(message = "El estado no puede estar vacío.")
    private String estado;
}
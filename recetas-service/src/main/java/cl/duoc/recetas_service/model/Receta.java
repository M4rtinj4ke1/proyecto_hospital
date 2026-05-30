package cl.duoc.recetas_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@AllArgsConstructor @NoArgsConstructor @Data
@Entity @Table(name = "recetas")
public class Receta {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La cita es obligatoria.")
    private Long citaId;

    @NotBlank(message = "El medicamento no puede estar vacío.")
    private String medicamento;

    @NotBlank(message = "La posología no puede estar vacía.")
    private String posologia;

    @NotNull(message = "La fecha de emisión es obligatoria.")
    private LocalDate fechaEmision;
}
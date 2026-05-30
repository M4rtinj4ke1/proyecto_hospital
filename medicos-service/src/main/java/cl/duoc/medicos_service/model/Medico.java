package cl.duoc.medicos_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "medicos")
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT no puede estar vacío.")
    @Size( max = 12)
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 30)
    private String nombre;

    @NotBlank(message = "Los apellidos no pueden estar vacíos.")
    @Size(max = 50)
    private String apellidos;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email
    private String email;

    @NotNull(message = "La especialidad es obligatoria.")
    private Long especialidadId;
}

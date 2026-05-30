package cl.duoc.pacientes_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El RUT no puede estar vacio.")
    @Size(max = 12, message = "RUT solo puede tener un maximo de 12 caracteres")
    @Column(unique = true, nullable = false)
    private String rut;

    @NotBlank(message = "Nombre no puede estar vacio.")
    @Size(max = 30, message = "Nombre solo puede tener un maximo de 30 caracteres")
    private String nombre;

    @NotBlank(message = "Apellidos no puede estar vacio.")
    @Size(max = 50, message = "Apellidos solo puede tener un maximo de 50 caracteres")
    private String apellidos;

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "Email no puede estar vacio.")
    @Email(message = "Debe ingresar un email valido.")
    private String email;
}
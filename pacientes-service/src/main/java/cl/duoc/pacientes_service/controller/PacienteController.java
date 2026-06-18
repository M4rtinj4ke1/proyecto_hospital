package cl.duoc.pacientes_service.controller;

import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.exception.ErrorResponse;
import cl.duoc.pacientes_service.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@Tag(name = "Pacientes", description = "Gestión de pacientes del sistema hospitalario")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @Operation(
            summary = "Lista todos los pacientes",
            description = "Devuelve la lista completa de pacientes registrados en el sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PacienteDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca un paciente por ID",
            description = "Retorna los datos de un paciente específico a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un paciente con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> findById(
            @Parameter(description = "ID del paciente a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra un nuevo paciente",
            description = "Crea un paciente nuevo con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente creado correctamente",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PacienteDTO> save(@RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza un paciente existente",
            description = "Modifica los datos de un paciente ya registrado, identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un paciente con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> update(
            @Parameter(description = "ID del paciente a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina un paciente",
            description = "Elimina de forma permanente el registro de un paciente según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Paciente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un paciente con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del paciente a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Busca un paciente por RUT",
            description = "Retorna los datos de un paciente a partir de su número de RUT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un paciente con el RUT proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<PacienteDTO> findByRut(
            @Parameter(description = "RUT del paciente, con guion y dígito verificador", example = "12345678-9")
            @PathVariable String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @Operation(
            summary = "Busca pacientes por apellido",
            description = "Retorna la lista de pacientes cuyo apellido coincide total o parcialmente con el valor entregado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacientes encontrados",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron pacientes con ese apellido",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/apellidos/{apellidos}")
    public ResponseEntity<List<PacienteDTO>> findByApellidos(
            @Parameter(description = "Apellido o parte del apellido a buscar", example = "González")
            @PathVariable String apellidos) {
        return ResponseEntity.ok(service.findByApellidos(apellidos));
    }

    @Operation(
            summary = "Busca un paciente por email",
            description = "Retorna los datos de un paciente a partir de su dirección de correo electrónico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado",
                    content = @Content(schema = @Schema(implementation = PacienteDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un paciente con ese email",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<PacienteDTO> findByEmail(
            @Parameter(description = "Correo electrónico del paciente", example = "juan.gonzalez@email.com")
            @PathVariable String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }
}
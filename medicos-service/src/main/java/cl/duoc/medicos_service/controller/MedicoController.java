package cl.duoc.medicos_service.controller;

import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.exception.ErrorResponse;
import cl.duoc.medicos_service.service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/medicos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Médicos", description = "Gestión de médicos del sistema hospitalario")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @Operation(
            summary = "Lista todos los médicos",
            description = "Devuelve la lista completa de médicos registrados, incluyendo los datos de su especialidad."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<MedicoDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca un médico por ID",
            description = "Retorna los datos de un médico específico a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico encontrado",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un médico con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> findById(
            @Parameter(description = "ID del médico a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra un nuevo médico",
            description = "Crea un médico nuevo con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Médico creado correctamente",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicoDTO> save(@RequestBody @Valid MedicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza un médico existente",
            description = "Modifica los datos de un médico ya registrado, identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un médico con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MedicoDTO> update(
            @Parameter(description = "ID del médico a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid MedicoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina un médico",
            description = "Elimina de forma permanente el registro de un médico según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Médico eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un médico con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del médico a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Busca un médico por RUT",
            description = "Retorna los datos de un médico a partir de su número de RUT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico encontrado",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un médico con el RUT proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/rut/{rut}")
    public ResponseEntity<MedicoDTO> findByRut(
            @Parameter(description = "RUT del médico, con guion y dígito verificador", example = "12345678-9")
            @PathVariable String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @Operation(
            summary = "Busca médicos por especialidad",
            description = "Retorna la lista de médicos asociados a una especialidad específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médicos encontrados",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron médicos para esa especialidad",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<MedicoDTO>> findByEspecialidad(
            @Parameter(description = "ID de la especialidad a buscar", example = "1")
            @PathVariable Long especialidadId) {
        return ResponseEntity.ok(service.findByEspecialidad(especialidadId));
    }

    @Operation(
            summary = "Busca médicos por nombre",
            description = "Retorna la lista de médicos cuyo nombre coincide total o parcialmente con el valor entregado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médicos encontrados",
                    content = @Content(schema = @Schema(implementation = MedicoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron médicos con ese nombre",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<MedicoDTO>> findByNombre(
            @Parameter(description = "Nombre o parte del nombre a buscar", example = "Carlos")
            @PathVariable String nombre) {
        return ResponseEntity.ok(service.findByNombre(nombre));
    }
}
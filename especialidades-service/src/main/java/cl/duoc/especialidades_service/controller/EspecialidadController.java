package cl.duoc.especialidades_service.controller;

import cl.duoc.especialidades_service.dto.EspecialidadDTO;
import cl.duoc.especialidades_service.exception.ErrorResponse;
import cl.duoc.especialidades_service.service.EspecialidadService;
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
@RequestMapping(value = "/api/especialidades", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Especialidades", description = "Gestión de especialidades médicas del sistema hospitalario")
public class EspecialidadController {

    private final EspecialidadService service;

    public EspecialidadController(EspecialidadService service) {
        this.service = service;
    }

    @Operation(
            summary = "Lista todas las especialidades",
            description = "Devuelve la lista completa de especialidades médicas registradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = EspecialidadDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<EspecialidadDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca una especialidad por ID",
            description = "Retorna los datos de una especialidad específica a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Especialidad encontrada",
                    content = @Content(schema = @Schema(implementation = EspecialidadDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una especialidad con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<EspecialidadDTO> findById(
            @Parameter(description = "ID de la especialidad a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra una nueva especialidad",
            description = "Crea una especialidad médica nueva con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Especialidad creada correctamente",
                    content = @Content(schema = @Schema(implementation = EspecialidadDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EspecialidadDTO> save(@RequestBody @Valid EspecialidadDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza una especialidad existente",
            description = "Modifica los datos de una especialidad ya registrada, identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Especialidad actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = EspecialidadDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una especialidad con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<EspecialidadDTO> update(
            @Parameter(description = "ID de la especialidad a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid EspecialidadDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina una especialidad",
            description = "Elimina de forma permanente el registro de una especialidad según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Especialidad eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una especialidad con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la especialidad a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
package cl.duoc.recetas_service.controller;

import cl.duoc.recetas_service.dto.RecetaDTO;
import cl.duoc.recetas_service.exception.ErrorResponse;
import cl.duoc.recetas_service.service.RecetaService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/recetas", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Recetas", description = "Gestión de recetas médicas del sistema hospitalario")
public class RecetaController {

    @Autowired
    private RecetaService service;

    @Operation(
            summary = "Lista todas las recetas",
            description = "Devuelve la lista completa de recetas médicas registradas, incluyendo los datos de la cita asociada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = RecetaDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<RecetaDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca una receta por ID",
            description = "Retorna los datos de una receta médica específica a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta encontrada",
                    content = @Content(schema = @Schema(implementation = RecetaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una receta con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<RecetaDTO> findById(
            @Parameter(description = "ID de la receta a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra una nueva receta",
            description = "Crea una receta médica nueva asociada a una cita, con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Receta creada correctamente",
                    content = @Content(schema = @Schema(implementation = RecetaDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "La cita referenciada no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecetaDTO> save(@RequestBody @Valid RecetaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza una receta existente",
            description = "Modifica los datos de una receta médica ya registrada, identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Receta actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = RecetaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una receta con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RecetaDTO> update(
            @Parameter(description = "ID de la receta a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid RecetaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina una receta",
            description = "Elimina de forma permanente el registro de una receta médica según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Receta eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una receta con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la receta a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
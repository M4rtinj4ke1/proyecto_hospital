package cl.duoc.notificaciones_service.controller;

import cl.duoc.notificaciones_service.dto.NotificacionDTO;
import cl.duoc.notificaciones_service.exception.ErrorResponse;
import cl.duoc.notificaciones_service.service.NotificacionService;
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
@RequestMapping(value = "/api/notificaciones", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del sistema hospitalario")
public class NotificacionController {

    @Autowired
    private NotificacionService service;

    @Operation(
            summary = "Lista todas las notificaciones",
            description = "Devuelve la lista completa de notificaciones registradas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca una notificación por ID",
            description = "Retorna los datos de una notificación específica a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación encontrada",
                    content = @Content(schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una notificación con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionDTO> findById(
            @Parameter(description = "ID de la notificación a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra una nueva notificación",
            description = "Crea una notificación nueva con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Notificación creada correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificacionDTO> save(@RequestBody @Valid NotificacionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza una notificación existente",
            description = "Modifica los datos de una notificación ya registrada, identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = NotificacionDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una notificación con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NotificacionDTO> update(
            @Parameter(description = "ID de la notificación a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid NotificacionDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina una notificación",
            description = "Elimina de forma permanente el registro de una notificación según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Notificación eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una notificación con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la notificación a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
package cl.duoc.pagos_service.controller;

import cl.duoc.pagos_service.dto.PagoDTO;
import cl.duoc.pagos_service.exception.ErrorResponse;
import cl.duoc.pagos_service.service.PagoService;
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
@RequestMapping(value = "/api/pagos", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Pagos", description = "Gestión de pagos del sistema hospitalario")
public class PagoController {

    @Autowired
    private PagoService service;

    @Operation(
            summary = "Lista todos los pagos",
            description = "Devuelve la lista completa de pagos registrados, incluyendo los datos de la cita asociada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = PagoDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<PagoDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca un pago por ID",
            description = "Retorna los datos de un pago específico a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado",
                    content = @Content(schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un pago con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> findById(
            @Parameter(description = "ID del pago a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra un nuevo pago",
            description = "Crea un pago nuevo asociado a una cita, con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago creado correctamente",
                    content = @Content(schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "La cita referenciada no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagoDTO> save(@RequestBody @Valid PagoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza un pago existente",
            description = "Modifica los datos de un pago ya registrado, identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un pago con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagoDTO> update(
            @Parameter(description = "ID del pago a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid PagoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina un pago",
            description = "Elimina de forma permanente el registro de un pago según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pago eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un pago con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del pago a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Busca pagos por cita",
            description = "Retorna la lista de pagos asociados a una cita específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagos encontrados",
                    content = @Content(schema = @Schema(implementation = PagoDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron pagos para esa cita",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<List<PagoDTO>> findByCita(
            @Parameter(description = "ID de la cita asociada a los pagos", example = "1")
            @PathVariable Long citaId) {
        return ResponseEntity.ok(service.findByCita(citaId));
    }
}
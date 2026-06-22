package cl.duoc.historial_service.controller;

import cl.duoc.historial_service.dto.HistorialDTO;
import cl.duoc.historial_service.exception.ErrorResponse;
import cl.duoc.historial_service.service.HistorialService;
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
@RequestMapping(value = "/api/historiales", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Historiales", description = "Gestión de historiales médicos del sistema hospitalario")
public class HistorialController {

    @Autowired
    private HistorialService service;

    @Operation(
            summary = "Lista todos los historiales",
            description = "Devuelve la lista completa de historiales médicos registrados, incluyendo los datos del paciente y del médico asociado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<HistorialDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Busca un historial por ID",
            description = "Retorna los datos de un historial médico específico a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial encontrado",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un historial con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<HistorialDTO> findById(
            @Parameter(description = "ID del historial a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Registra un nuevo historial",
            description = "Crea un registro de historial médico con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Historial creado correctamente",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "El paciente o médico referenciado no existe",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistorialDTO> save(@RequestBody @Valid HistorialDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @Operation(
            summary = "Actualiza un historial existente",
            description = "Modifica los datos de un historial médico ya registrado, identificado por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial actualizado correctamente",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe un historial con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HistorialDTO> update(
            @Parameter(description = "ID del historial a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid HistorialDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
            summary = "Elimina un historial",
            description = "Elimina de forma permanente el registro de un historial médico según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Historial eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe un historial con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID del historial a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Busca historiales por paciente",
            description = "Retorna la lista de historiales médicos asociados a un paciente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historiales encontrados",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron historiales para ese paciente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<HistorialDTO>> findByPaciente(
            @Parameter(description = "ID del paciente asociado a los historiales", example = "1")
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.findByPaciente(pacienteId));
    }

    @Operation(
            summary = "Busca historiales por médico",
            description = "Retorna la lista de historiales médicos asociados a un médico específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historiales encontrados",
                    content = @Content(schema = @Schema(implementation = HistorialDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron historiales para ese médico",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/medico/{medicoId}")
    public ResponseEntity<List<HistorialDTO>> findByMedico(
            @Parameter(description = "ID del médico asociado a los historiales", example = "1")
            @PathVariable Long medicoId) {
        return ResponseEntity.ok(service.findByMedico(medicoId));
    }
}
package cl.duoc.citas_service.controller;

import cl.duoc.citas_service.dto.CitaDTO;
import cl.duoc.citas_service.exception.ErrorResponse;
import cl.duoc.citas_service.model.Cita;
import cl.duoc.citas_service.service.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/citas", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Citas", description = "Gestión de citas médicas del sistema hospitalario")
public class CitaController {

    @Autowired
    private CitaService service;

    @Operation(
            summary = "Lista todas las citas",
            description = "Devuelve la lista completa de citas registradas, incluyendo los datos del paciente asociado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente",
                    content = @Content(schema = @Schema(implementation = CitaDTO.class)))
    })
    @GetMapping
    public ResponseEntity<List<CitaDTO>> findAll() {
        return new ResponseEntity<>(service.findAll(), HttpStatus.OK);
    }

    @Operation(
            summary = "Busca una cita por ID",
            description = "Retorna los datos de una cita específica a partir de su identificador único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita encontrada",
                    content = @Content(schema = @Schema(implementation = CitaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No existe una cita con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> findById(
            @Parameter(description = "ID de la cita a buscar", example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Agenda una nueva cita",
            description = "Crea una nueva cita médica con los datos enviados en el cuerpo de la solicitud."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cita creada correctamente",
                    content = @Content(schema = @Schema(implementation = Cita.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cita> save(@RequestBody @Valid Cita cita) {
        return new ResponseEntity<>(service.save(cita), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Elimina una cita",
            description = "Elimina de forma permanente el registro de una cita según su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cita eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "No existe una cita con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @Parameter(description = "ID de la cita a eliminar", example = "1")
            @PathVariable Long id) {
        service.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Actualiza una cita existente",
            description = "Modifica los datos de una cita ya registrada, identificada por su ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cita actualizada correctamente",
                    content = @Content(schema = @Schema(implementation = Cita.class))),
            @ApiResponse(responseCode = "404", description = "No existe una cita con el ID proporcionado",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Cita> update(
            @Parameter(description = "ID de la cita a actualizar", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid Cita cita) {
        return ResponseEntity.ok(service.update(id, cita));
    }

    @Operation(
            summary = "Busca citas por paciente",
            description = "Retorna la lista de citas asociadas a un paciente específico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Citas encontradas",
                    content = @Content(schema = @Schema(implementation = CitaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron citas para ese paciente",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaDTO>> findByPaciente(
            @Parameter(description = "ID del paciente asociado a las citas", example = "1")
            @PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.findByPaciente(pacienteId));
    }

    @Operation(
            summary = "Busca citas por fecha",
            description = "Retorna la lista de citas agendadas en una fecha específica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Citas encontradas",
                    content = @Content(schema = @Schema(implementation = CitaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron citas para esa fecha",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<CitaDTO>> findByFecha(
            @Parameter(description = "Fecha de la cita en formato YYYY-MM-DD", example = "2026-07-15")
            @PathVariable LocalDate fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @Operation(
            summary = "Busca citas entre dos fechas",
            description = "Retorna la lista de citas agendadas dentro de un rango de fechas, ambos límites inclusive."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Citas encontradas",
                    content = @Content(schema = @Schema(implementation = CitaDTO.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron citas en ese rango de fechas",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/entre-fechas/{inicio}/{fin}")
    public ResponseEntity<List<CitaDTO>> findEntreFechas(
            @Parameter(description = "Fecha de inicio del rango, formato YYYY-MM-DD", example = "2026-07-01")
            @PathVariable LocalDate inicio,
            @Parameter(description = "Fecha de fin del rango, formato YYYY-MM-DD", example = "2026-07-31")
            @PathVariable LocalDate fin) {
        return ResponseEntity.ok(service.findEntreFechas(inicio, fin));
    }
}
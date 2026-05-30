package cl.duoc.citas_service.controller;
import cl.duoc.citas_service.dto.CitaDTO;
import cl.duoc.citas_service.model.Cita;
import cl.duoc.citas_service.service.CitaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController @RequestMapping("/api/citas")
public class CitaController {
    @Autowired private CitaService service;

    @GetMapping
    public ResponseEntity<List<CitaDTO>> findAll() { return new ResponseEntity<>(service.findAll(), HttpStatus.OK); }
    @GetMapping("/{id}")
    public ResponseEntity<CitaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }
    @PostMapping
    public ResponseEntity<Cita> save(@RequestBody @Valid Cita cita) { return new ResponseEntity<>(service.save(cita), HttpStatus.CREATED); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) { service.deleteById(id); return new ResponseEntity<>(HttpStatus.NO_CONTENT); }

    @PutMapping("/{id}")
    public ResponseEntity<Cita> update(@PathVariable Long id, @RequestBody @Valid Cita cita) {
        return ResponseEntity.ok(service.update(id, cita));
    }
    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<CitaDTO>> findByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(service.findByPaciente(pacienteId));
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<CitaDTO>> findByFecha(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(service.findByFecha(fecha));
    }

    @GetMapping("/entre-fechas/{inicio}/{fin}")
    public ResponseEntity<List<CitaDTO>> findEntreFechas(
            @PathVariable LocalDate inicio,
            @PathVariable LocalDate fin) {
        return ResponseEntity.ok(service.findEntreFechas(inicio, fin));
    }
}
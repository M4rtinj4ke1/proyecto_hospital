package cl.duoc.pacientes_service.controller;

import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    @Autowired
    private PacienteService service;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> save(@RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> update(@PathVariable Long id, @RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/rut/{rut}")
    public ResponseEntity<PacienteDTO> findByRut(@PathVariable String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @GetMapping("/apellidos/{apellidos}")
    public ResponseEntity<List<PacienteDTO>> findByApellidos(@PathVariable String apellidos) {
        return ResponseEntity.ok(service.findByApellidos(apellidos));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<PacienteDTO> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.findByEmail(email));
    }
}
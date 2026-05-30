package cl.duoc.medicos_service.controller;

import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    private final MedicoService service;

    public MedicoController(MedicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<MedicoDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<MedicoDTO> save(@RequestBody @Valid MedicoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoDTO> update(@PathVariable Long id, @RequestBody @Valid MedicoDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/rut/{rut}")
    public ResponseEntity<MedicoDTO> findByRut(@PathVariable String rut) {
        return ResponseEntity.ok(service.findByRut(rut));
    }

    @GetMapping("/especialidad/{especialidadId}")
    public ResponseEntity<List<MedicoDTO>> findByEspecialidad(@PathVariable Long especialidadId) {
        return ResponseEntity.ok(service.findByEspecialidad(especialidadId));
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<List<MedicoDTO>> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(service.findByNombre(nombre));
    }
}
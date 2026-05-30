package cl.duoc.pacientes_service.service;

import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.mapper.PacienteMapper;
import cl.duoc.pacientes_service.model.Paciente;
import cl.duoc.pacientes_service.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.pacientes_service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PacienteService {

    @Autowired
    private PacienteRepository repository;

    @Autowired
    private PacienteMapper mapper;

    public List<PacienteDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public PacienteDTO findById(Long id) {
        Paciente p = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
        return mapper.toDTO(p);
    }

    public PacienteDTO save(PacienteDTO dto) {
        Paciente saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    public PacienteDTO update(Long id, PacienteDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
        dto.setId(id);
        return mapper.toDTO(repository.save(mapper.toEntity(dto)));
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con ID: " + id));
    }

    public PacienteDTO findByRut(String rut) {
        Paciente p = repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con RUT: " + rut));
        return mapper.toDTO(p);
    }

    public List<PacienteDTO> findByApellidos(String apellidos) {
        List<Paciente> pacientes = repository.findByApellidosContainingIgnoreCase(apellidos);
        if (pacientes.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron pacientes con apellido: " + apellidos);
        }
        return pacientes.stream().map(mapper::toDTO).collect(Collectors.toList());
    }

    public PacienteDTO findByEmail(String email) {
        Paciente p = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con email: " + email));
        return mapper.toDTO(p);
    }
}
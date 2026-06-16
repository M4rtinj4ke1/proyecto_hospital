package cl.duoc.historial_service.service;

import cl.duoc.historial_service.clients.MedicoFeign;
import cl.duoc.historial_service.clients.PacienteFeign;
import cl.duoc.historial_service.dto.HistorialDTO;
import cl.duoc.historial_service.dto.MedicoDTO;
import cl.duoc.historial_service.dto.PacienteDTO;
import cl.duoc.historial_service.exception.ResourceNotFoundException;
import cl.duoc.historial_service.mapper.HistorialMapper;
import cl.duoc.historial_service.model.Historial;
import cl.duoc.historial_service.repository.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repository;

    @Autowired
    private HistorialMapper mapper;

    @Autowired
    private PacienteFeign pacienteFeign;

    @Autowired
    private MedicoFeign medicoFeign;

    private PacienteDTO obtenerPaciente(Long id) {
        try {
            return pacienteFeign.findById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Paciente no encontrado con ID: " + id);
        }
    }

    private MedicoDTO obtenerMedico(Long id) {
        try {
            return medicoFeign.findById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Médico no encontrado con ID: " + id);
        }
    }

    public List<HistorialDTO> findAll() {
        return repository.findAll().stream().map(h ->
                mapper.toDTO(h, obtenerPaciente(h.getPacienteId()), obtenerMedico(h.getMedicoId()))
        ).collect(Collectors.toList());
    }

    public HistorialDTO findById(Long id) {
        Historial h = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con ID: " + id));
        return mapper.toDTO(h, obtenerPaciente(h.getPacienteId()), obtenerMedico(h.getMedicoId()));
    }

    public HistorialDTO save(HistorialDTO dto) {
        obtenerPaciente(dto.getPaciente().getId());
        obtenerMedico(dto.getMedico().getId());

        Historial saved = repository.save(mapper.toEntity(dto));
        return findById(saved.getId());
    }

    public HistorialDTO update(Long id, HistorialDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con ID: " + id));
        obtenerPaciente(dto.getPaciente().getId());
        obtenerMedico(dto.getMedico().getId());

        dto.setId(id);
        Historial updated = repository.save(mapper.toEntity(dto));
        return findById(updated.getId());
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado con ID: " + id));
        repository.deleteById(id);
    }
}
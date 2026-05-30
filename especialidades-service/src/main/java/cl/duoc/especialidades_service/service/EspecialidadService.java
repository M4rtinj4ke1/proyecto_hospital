package cl.duoc.especialidades_service.service;

import cl.duoc.especialidades_service.Repository.EspecialidadRepository;
import cl.duoc.especialidades_service.dto.EspecialidadDTO;
import cl.duoc.especialidades_service.mapper.EspecialidadMapper;
import cl.duoc.especialidades_service.model.Especialidad;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EspecialidadService {

    private final EspecialidadRepository repository;
    private final EspecialidadMapper mapper;

    public EspecialidadService(EspecialidadRepository repository, EspecialidadMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<EspecialidadDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public EspecialidadDTO findById(Long id) {
        Especialidad e = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
        return mapper.toDTO(e);
    }

    public EspecialidadDTO save(EspecialidadDTO dto) {
        Especialidad saved = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    public EspecialidadDTO update(Long id, EspecialidadDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
        dto.setId(id);
        Especialidad updated = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(updated);
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada con ID: " + id));
        repository.deleteById(id);
    }
}
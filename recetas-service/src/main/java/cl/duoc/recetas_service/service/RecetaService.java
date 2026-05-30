package cl.duoc.recetas_service.service;

import cl.duoc.recetas_service.clients.CitaFeign;
import cl.duoc.recetas_service.dto.CitaDTO;
import cl.duoc.recetas_service.dto.RecetaDTO;
import cl.duoc.recetas_service.mapper.RecetaMapper;
import cl.duoc.recetas_service.model.Receta;
import cl.duoc.recetas_service.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository repository;

    @Autowired
    private RecetaMapper mapper;

    @Autowired
    private CitaFeign citaFeign;

    private CitaDTO obtenerCita(Long id) {
        try {
            return citaFeign.findById(id);
        } catch (Exception e) {
            return new CitaDTO(id, null, "No disponible", null);
        }
    }

    public List<RecetaDTO> findAll() {
        return repository.findAll().stream()
                .map(r -> mapper.toDTO(r, obtenerCita(r.getCitaId())))
                .collect(Collectors.toList());
    }

    public RecetaDTO findById(Long id) {
        Receta r = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
        return mapper.toDTO(r, obtenerCita(r.getCitaId()));
    }

    public RecetaDTO save(RecetaDTO dto) {
        Receta saved = repository.save(mapper.toEntity(dto));
        return findById(saved.getId());
    }

    public RecetaDTO update(Long id, RecetaDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
        dto.setId(id);
        Receta updated = repository.save(mapper.toEntity(dto));
        return findById(updated.getId());
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Receta no encontrada con ID: " + id));
        repository.deleteById(id);
    }
}
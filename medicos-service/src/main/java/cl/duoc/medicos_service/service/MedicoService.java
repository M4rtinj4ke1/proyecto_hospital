package cl.duoc.medicos_service.service;

import cl.duoc.medicos_service.clients.EspecialidadFeign;
import cl.duoc.medicos_service.dto.EspecialidadDTO;
import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.mapper.MedicoMapper;
import cl.duoc.medicos_service.model.Medico;
import cl.duoc.medicos_service.repository.MedicoRepository;
import org.springframework.stereotype.Service;
import cl.duoc.medicos_service.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MedicoService {

    private final MedicoRepository repository;
    private final MedicoMapper mapper;
    private final EspecialidadFeign especialidadFeign;

    public MedicoService(MedicoRepository repository, MedicoMapper mapper, EspecialidadFeign especialidadFeign) {
        this.repository = repository;
        this.mapper = mapper;
        this.especialidadFeign = especialidadFeign;
    }

    public List<MedicoDTO> findAll() {
        return repository.findAll().stream().map(medico -> {
            EspecialidadDTO especialidad = null;
            try {
                especialidad = especialidadFeign.findById(medico.getEspecialidadId());
            } catch (Exception e) {
                especialidad = new EspecialidadDTO(medico.getEspecialidadId(), "No disponible", "");
            }
            return mapper.toDTO(medico, especialidad);
        }).collect(Collectors.toList());
    }

    public MedicoDTO findById(Long id) {
        Medico medico = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + id));
        EspecialidadDTO especialidad = null;
        try {
            especialidad = especialidadFeign.findById(medico.getEspecialidadId());
        } catch (Exception e) {
            especialidad = new EspecialidadDTO(medico.getEspecialidadId(), "No disponible", "");
        }
        return mapper.toDTO(medico, especialidad);
    }

    public MedicoDTO save(MedicoDTO dto) {
        Medico saved = repository.save(mapper.toEntity(dto));
        return findById(saved.getId());
    }

    public MedicoDTO update(Long id, MedicoDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + id));
        dto.setId(id);
        Medico updated = repository.save(mapper.toEntity(dto));
        return findById(updated.getId());
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con ID: " + id));
        repository.deleteById(id);
    }

    public MedicoDTO findByRut(String rut) {
        Medico m = repository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Médico no encontrado con RUT: " + rut));
        EspecialidadDTO especialidad = null;
        try {
            especialidad = especialidadFeign.findById(m.getEspecialidadId());
        } catch (Exception e) {
            especialidad = new EspecialidadDTO(m.getEspecialidadId(), "No disponible", "");
        }
        return mapper.toDTO(m, especialidad);
    }

    public List<MedicoDTO> findByEspecialidad(Long especialidadId) {
        List<Medico> medicos = repository.findByEspecialidadId(especialidadId);
        if (medicos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron médicos para la especialidad con ID: " + especialidadId);
        }
        return medicos.stream().map(m -> {
            EspecialidadDTO especialidad = null;
            try { especialidad = especialidadFeign.findById(m.getEspecialidadId()); }
            catch (Exception e) { especialidad = new EspecialidadDTO(m.getEspecialidadId(), "No disponible", ""); }
            return mapper.toDTO(m, especialidad);
        }).collect(Collectors.toList());
    }

    public List<MedicoDTO> findByNombre(String nombre) {
        List<Medico> medicos = repository.findByNombreContainingIgnoreCase(nombre);
        if (medicos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron médicos con nombre: " + nombre);
        }
        return medicos.stream().map(m -> {
            EspecialidadDTO especialidad = null;
            try { especialidad = especialidadFeign.findById(m.getEspecialidadId()); }
            catch (Exception e) { especialidad = new EspecialidadDTO(m.getEspecialidadId(), "No disponible", ""); }
            return mapper.toDTO(m, especialidad);
        }).collect(Collectors.toList());
    }
}

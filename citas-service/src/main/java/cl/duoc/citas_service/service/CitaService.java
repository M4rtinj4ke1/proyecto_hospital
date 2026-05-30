package cl.duoc.citas_service.service;

import cl.duoc.citas_service.clients.PacienteFeign;
import cl.duoc.citas_service.dto.CitaDTO;
import cl.duoc.citas_service.dto.PacienteDTO;
import cl.duoc.citas_service.mapper.CitaMapper;
import cl.duoc.citas_service.model.Cita;
import cl.duoc.citas_service.repository.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cl.duoc.citas_service.exception.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaService {

    @Autowired
    private CitaRepository repository;

    @Autowired
    private PacienteFeign pacienteFeign;

    @Autowired
    private CitaMapper mapper; // <-- Inyectamos tu nuevo Mapper

    public List<CitaDTO> findAll() {
        List<Cita> citas = repository.findAll();
        List<CitaDTO> dtos = new ArrayList<>();

        for (Cita cita : citas) {
            PacienteDTO paciente = null;
            try {
                paciente = pacienteFeign.obtenerPacientePorId(cita.getPacienteId());
            } catch (Exception e) {
            }
            dtos.add(mapper.toDTO(cita, paciente));
        }
        return dtos;
    }

    public CitaDTO findById(Long id) {
        Cita cita = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        PacienteDTO paciente = null;
        try {
            paciente = pacienteFeign.obtenerPacientePorId(cita.getPacienteId());
        } catch (Exception e) {}

        return mapper.toDTO(cita, paciente);
    }

    public Cita save(Cita cita) {
        return repository.save(cita);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public Cita update(Long id, Cita citaActualizada) {
        Cita original = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con ID: " + id));

        original.setFecha(citaActualizada.getFecha());
        original.setDescripcion(citaActualizada.getDescripcion());
        original.setPacienteId(citaActualizada.getPacienteId());
        return repository.save(original);
    }

    public List<CitaDTO> findByPaciente(Long pacienteId) {
        List<Cita> citas = repository.findByPacienteId(pacienteId);
        if (citas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron citas para el paciente con ID: " + pacienteId);
        }
        return citas.stream().map(cita -> {
            PacienteDTO paciente = null;
            try { paciente = pacienteFeign.obtenerPacientePorId(cita.getPacienteId()); } catch (Exception e) {}
            return mapper.toDTO(cita, paciente);
        }).collect(Collectors.toList());
    }

    public List<CitaDTO> findByFecha(LocalDate fecha) {
        List<Cita> citas = repository.findByFecha(fecha);
        if (citas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron citas para la fecha: " + fecha);
        }
        return citas.stream().map(cita -> {
            PacienteDTO paciente = null;
            try { paciente = pacienteFeign.obtenerPacientePorId(cita.getPacienteId()); } catch (Exception e) {}
            return mapper.toDTO(cita, paciente);
        }).collect(Collectors.toList());
    }

    public List<CitaDTO> findEntreFechas(LocalDate inicio, LocalDate fin) {
        List<Cita> citas = repository.findCitasEntreFechas(inicio, fin);
        if (citas.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron citas entre " + inicio + " y " + fin);
        }
        return citas.stream().map(cita -> {
            PacienteDTO paciente = null;
            try { paciente = pacienteFeign.obtenerPacientePorId(cita.getPacienteId()); } catch (Exception e) {}
            return mapper.toDTO(cita, paciente);
        }).collect(Collectors.toList());
    }
}
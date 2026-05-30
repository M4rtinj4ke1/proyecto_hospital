package cl.duoc.notificaciones_service.service;

import cl.duoc.notificaciones_service.clients.PacienteFeign;
import cl.duoc.notificaciones_service.dto.NotificacionDTO;
import cl.duoc.notificaciones_service.dto.PacienteDTO;
import cl.duoc.notificaciones_service.mapper.NotificacionMapper;
import cl.duoc.notificaciones_service.model.Notificacion;
import cl.duoc.notificaciones_service.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificacionService {

    @Autowired
    private NotificacionRepository repository;

    @Autowired
    private NotificacionMapper mapper;

    @Autowired
    private PacienteFeign pacienteFeign;

    private PacienteDTO obtenerPaciente(Long id) {
        try {
            return pacienteFeign.findById(id);
        } catch (Exception e) {
            return new PacienteDTO(id, "No disponible", "", "", null, "");
        }
    }

    private void simularEnvio(Notificacion n, PacienteDTO paciente) {
        System.out.println("//////////////////////////////////////////////////////");
        System.out.println("📧 SIMULACIÓN DE ENVÍO DE NOTIFICACIÓN");
        System.out.println("Tipo     : " + n.getTipo());
        System.out.println("Para     : " + paciente.getNombre() + " " + paciente.getApellidos());
        System.out.println("Email    : " + paciente.getEmail());
        System.out.println("Mensaje  : " + n.getMensaje());
        System.out.println("Fecha    : " + n.getFechaEnvio());
        System.out.println("//////////////////////////////////////////////////////");
    }

    public List<NotificacionDTO> findAll() {
        return repository.findAll().stream()
                .map(n -> mapper.toDTO(n, obtenerPaciente(n.getPacienteId())))
                .collect(Collectors.toList());
    }

    public NotificacionDTO findById(Long id) {
        Notificacion n = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        return mapper.toDTO(n, obtenerPaciente(n.getPacienteId()));
    }

    public NotificacionDTO save(NotificacionDTO dto) {
        Notificacion n = mapper.toEntity(dto);
        n.setFechaEnvio(LocalDateTime.now());
        Notificacion saved = repository.save(n);
        PacienteDTO paciente = obtenerPaciente(saved.getPacienteId());
        simularEnvio(saved, paciente);
        return mapper.toDTO(saved, paciente);
    }

    public NotificacionDTO update(Long id, NotificacionDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        dto.setId(id);
        Notificacion updated = repository.save(mapper.toEntity(dto));
        return mapper.toDTO(updated, obtenerPaciente(updated.getPacienteId()));
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada con ID: " + id));
        repository.deleteById(id);
    }
}
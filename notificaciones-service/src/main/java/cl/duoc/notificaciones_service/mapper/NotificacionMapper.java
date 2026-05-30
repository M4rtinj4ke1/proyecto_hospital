package cl.duoc.notificaciones_service.mapper;

import cl.duoc.notificaciones_service.dto.NotificacionDTO;
import cl.duoc.notificaciones_service.dto.PacienteDTO;
import cl.duoc.notificaciones_service.model.Notificacion;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public NotificacionDTO toDTO(Notificacion notificacion, PacienteDTO paciente) {
        return new NotificacionDTO(
                notificacion.getId(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio(),
                notificacion.getTipo(),
                paciente
        );
    }

    public Notificacion toEntity(NotificacionDTO dto) {
        Notificacion n = new Notificacion();
        n.setId(dto.getId());
        n.setPacienteId(dto.getPaciente() != null ? dto.getPaciente().getId() : null);
        n.setMensaje(dto.getMensaje());
        n.setFechaEnvio(dto.getFechaEnvio());
        n.setTipo(dto.getTipo());
        return n;
    }
}
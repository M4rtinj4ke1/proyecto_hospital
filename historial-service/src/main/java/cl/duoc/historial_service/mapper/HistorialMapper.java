package cl.duoc.historial_service.mapper;

import cl.duoc.historial_service.dto.HistorialDTO;
import cl.duoc.historial_service.dto.MedicoDTO;
import cl.duoc.historial_service.dto.PacienteDTO;
import cl.duoc.historial_service.model.Historial;
import org.springframework.stereotype.Component;

@Component
public class HistorialMapper {
    public HistorialDTO toDTO(Historial historial, PacienteDTO paciente, MedicoDTO medico) {
        return new HistorialDTO(
                historial.getId(),
                historial.getFecha(),
                historial.getDiagnostico(),
                historial.getTratamiento(),
                paciente,
                medico
        );
    }

    public Historial toEntity(HistorialDTO dto) {
        Historial h = new Historial();
        h.setId(dto.getId());
        h.setPacienteId(dto.getPaciente() != null ? dto.getPaciente().getId() : null);
        h.setMedicoId(dto.getMedico() != null ? dto.getMedico().getId() : null);
        h.setFecha(dto.getFecha());
        h.setDiagnostico(dto.getDiagnostico());
        h.setTratamiento(dto.getTratamiento());
        return h;
    }
}
package cl.duoc.citas_service.mapper;

import cl.duoc.citas_service.dto.CitaDTO;
import cl.duoc.citas_service.dto.PacienteDTO;
import cl.duoc.citas_service.model.Cita;
import org.springframework.stereotype.Component;

@Component
public class CitaMapper {

    public CitaDTO toDTO(Cita cita, PacienteDTO paciente) {
        if (cita == null) {
            return null;
        }

        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFecha(cita.getFecha());
        dto.setDescripcion(cita.getDescripcion());
        dto.setPaciente(paciente);

        return dto;
    }
}
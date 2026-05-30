package cl.duoc.pacientes_service.mapper;

import cl.duoc.pacientes_service.dto.PacienteDTO;
import cl.duoc.pacientes_service.model.Paciente;
import org.springframework.stereotype.Component;

@Component
public class PacienteMapper {
    public PacienteDTO toDTO(Paciente paciente) {
        return new PacienteDTO(
                paciente.getId(),
                paciente.getRut(),
                paciente.getNombre(),
                paciente.getApellidos(),
                paciente.getFechaNacimiento(),
                paciente.getEmail()
        );
    }

    public Paciente toEntity(PacienteDTO dto) {
        return new Paciente(
                dto.getId(),
                dto.getRut(),
                dto.getNombre(),
                dto.getApellidos(),
                dto.getFechaNacimiento(),
                dto.getEmail()
        );
    }
}
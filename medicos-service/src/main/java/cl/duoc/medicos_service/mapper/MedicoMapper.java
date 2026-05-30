package cl.duoc.medicos_service.mapper;

import cl.duoc.medicos_service.dto.EspecialidadDTO;
import cl.duoc.medicos_service.dto.MedicoDTO;
import cl.duoc.medicos_service.model.Medico;
import org.springframework.stereotype.Component;

@Component
public class MedicoMapper {
    public MedicoDTO toDTO(Medico medico, EspecialidadDTO especialidad) {
        return new MedicoDTO(
                medico.getId(),
                medico.getRut(),
                medico.getNombre(),
                medico.getApellidos(),
                medico.getEmail(),
                especialidad
        );
    }

    public Medico toEntity(MedicoDTO dto) {
        Medico medico = new Medico();
        medico.setId(dto.getId());
        medico.setRut(dto.getRut());
        medico.setNombre(dto.getNombre());
        medico.setApellidos(dto.getApellidos());
        medico.setEmail(dto.getEmail());
        medico.setEspecialidadId(dto.getEspecialidad() != null ? dto.getEspecialidad().getId() : null);
        return medico;
    }
}

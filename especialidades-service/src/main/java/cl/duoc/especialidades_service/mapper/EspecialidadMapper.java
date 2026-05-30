package cl.duoc.especialidades_service.mapper;

import cl.duoc.especialidades_service.dto.EspecialidadDTO;
import cl.duoc.especialidades_service.model.Especialidad;
import org.springframework.stereotype.Component;

@Component
public class EspecialidadMapper {
    public EspecialidadDTO toDTO(Especialidad especialidad) {
        return new EspecialidadDTO(
                especialidad.getId(),
                especialidad.getNombre(),
                especialidad.getDescripcion()
        );
    }

    public Especialidad toEntity(EspecialidadDTO dto) {
        return new Especialidad(
                dto.getId(),
                dto.getNombre(),
                dto.getDescripcion()
        );
    }
}
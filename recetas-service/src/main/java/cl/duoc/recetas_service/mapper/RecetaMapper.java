package cl.duoc.recetas_service.mapper;

import cl.duoc.recetas_service.dto.CitaDTO;
import cl.duoc.recetas_service.dto.RecetaDTO;
import cl.duoc.recetas_service.model.Receta;
import org.springframework.stereotype.Component;

@Component
public class RecetaMapper {

    public RecetaDTO toDTO(Receta receta, CitaDTO cita) {
        return new RecetaDTO(
                receta.getId(),
                receta.getMedicamento(),
                receta.getPosologia(),
                receta.getFechaEmision(),
                cita
        );
    }

    public Receta toEntity(RecetaDTO dto) {
        Receta r = new Receta();
        r.setId(dto.getId());
        r.setCitaId(dto.getCita() != null ? dto.getCita().getId() : null);
        r.setMedicamento(dto.getMedicamento());
        r.setPosologia(dto.getPosologia());
        r.setFechaEmision(dto.getFechaEmision());
        return r;
    }
}
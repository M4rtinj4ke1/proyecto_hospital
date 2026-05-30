package cl.duoc.pagos_service.mapper;

import cl.duoc.pagos_service.dto.CitaDTO;
import cl.duoc.pagos_service.dto.PagoDTO;
import cl.duoc.pagos_service.model.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    public PagoDTO toDTO(Pago pago, CitaDTO cita) {
        return new PagoDTO(
                pago.getId(),
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getEstado(),
                cita
        );
    }

    public Pago toEntity(PagoDTO dto) {
        Pago p = new Pago();
        p.setId(dto.getId());
        p.setCitaId(dto.getCita() != null ? dto.getCita().getId() : null);
        p.setMonto(dto.getMonto());
        p.setMetodoPago(dto.getMetodoPago());
        p.setEstado(dto.getEstado());
        return p;
    }
}
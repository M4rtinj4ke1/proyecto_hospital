package cl.duoc.pagos_service.service;

import cl.duoc.pagos_service.clients.CitaFeign;
import cl.duoc.pagos_service.dto.CitaDTO;
import cl.duoc.pagos_service.dto.PagoDTO;
import cl.duoc.pagos_service.mapper.PagoMapper;
import cl.duoc.pagos_service.model.Pago;
import cl.duoc.pagos_service.repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repository;

    @Autowired
    private PagoMapper mapper;

    @Autowired
    private CitaFeign citaFeign;

    private CitaDTO obtenerCita(Long id) {
        try {
            return citaFeign.findById(id);
        } catch (Exception e) {
            return new CitaDTO(id, null, "No disponible", null);
        }
    }

    public List<PagoDTO> findAll() {
        return repository.findAll().stream()
                .map(p -> mapper.toDTO(p, obtenerCita(p.getCitaId())))
                .collect(Collectors.toList());
    }

    public PagoDTO findById(Long id) {
        Pago p = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        return mapper.toDTO(p, obtenerCita(p.getCitaId()));
    }

    public PagoDTO save(PagoDTO dto) {
        Pago saved = repository.save(mapper.toEntity(dto));
        return findById(saved.getId());
    }

    public PagoDTO update(Long id, PagoDTO dto) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        dto.setId(id);
        Pago updated = repository.save(mapper.toEntity(dto));
        return findById(updated.getId());
    }

    public void deleteById(Long id) {
        repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado con ID: " + id));
        repository.deleteById(id);
    }
}
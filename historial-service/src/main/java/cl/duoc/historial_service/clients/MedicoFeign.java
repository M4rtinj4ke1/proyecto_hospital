package cl.duoc.historial_service.clients;

import cl.duoc.historial_service.dto.MedicoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "medicos-service")
public interface MedicoFeign {
    @GetMapping("/api/medicos/{id}")
    MedicoDTO findById(@PathVariable("id") Long id);
}
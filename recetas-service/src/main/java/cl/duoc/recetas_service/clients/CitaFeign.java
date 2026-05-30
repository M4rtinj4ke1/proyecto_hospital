package cl.duoc.recetas_service.clients;

import cl.duoc.recetas_service.dto.CitaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "citas-service")
public interface CitaFeign {
    @GetMapping("/api/citas/{id}")
    CitaDTO findById(@PathVariable("id") Long id);
}

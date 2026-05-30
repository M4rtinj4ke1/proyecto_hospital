package cl.duoc.medicos_service.clients;

import cl.duoc.medicos_service.dto.EspecialidadDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "especialidades-service")
public interface EspecialidadFeign {
    @GetMapping("/api/especialidades/{id}")
    EspecialidadDTO findById(@PathVariable("id") Long id);
}

package cl.duoc.notificaciones_service.clients;

import cl.duoc.notificaciones_service.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "pacientes-service")
public interface PacienteFeign {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO findById(@PathVariable("id") Long id);
}
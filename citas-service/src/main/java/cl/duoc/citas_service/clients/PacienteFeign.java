package cl.duoc.citas_service.clients;
import cl.duoc.citas_service.dto.PacienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name = "pacientes-service")
public interface PacienteFeign {
    @GetMapping("/api/pacientes/{id}")
    PacienteDTO obtenerPacientePorId(@PathVariable("id") Long id);
}
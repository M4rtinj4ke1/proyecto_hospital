package cl.duoc.notificaciones_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor @Data
public class NotificacionDTO {
    private Long id;
    private String mensaje;
    private LocalDateTime fechaEnvio;
    private String tipo;
    private PacienteDTO paciente;
}
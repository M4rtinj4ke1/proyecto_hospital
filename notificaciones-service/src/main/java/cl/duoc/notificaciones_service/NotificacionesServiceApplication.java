package cl.duoc.notificaciones_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Notificiones service API",
				description = "DESCRIPTION " +
						"API para notificar a los pacientes del sistema hospitalario" +
						"- Registrar" +
						"- Consultar" +
						"- Actualizar" +
						"- Eliminar",

				version = "1.0.0",
				contact = @Contact(
						name =
								"Martin Jaque"
						,
						email = "ma.jaqueq@duocuc.cl"
				)
		)
)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.duoc.notificaciones_service.clients")
public class NotificacionesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificacionesServiceApplication.class, args);
	}
}
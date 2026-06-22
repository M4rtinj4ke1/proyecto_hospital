package cl.duoc.historial_service;

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
				title = "Historial service API",
				description = """
        API para consultar el historial médico de los pacientes del sistema hospitalario.

        Permite:
        - Registrar historiales
        - Consultar historiales
        - Actualizar historiales
        - Eliminar historiales
        """,
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
@EnableFeignClients(basePackages = "cl.duoc.historial_service.clients")
public class HistorialServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HistorialServiceApplication.class, args);
	}
}
package cl.duoc.citas_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
		info = @Info(
				title = "Citas Service API",
				version = "1.0.0",
				description = """
                         API REST para la gestión de citas médicas del sistema hospitalario.

                         Permite:
                         - Agendar citas
                         - Consultar citas
                         - Actualizar el estado de una cita
                         - Cancelar citas
                         """,
				contact = @Contact(
						name = "Martin Jaque",
						email = "ma.jaqueq@duocuc.cl"
				)
		)
)
@SpringBootApplication
@EnableDiscoveryClient
public class CitasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitasServiceApplication.class, args);
	}
}
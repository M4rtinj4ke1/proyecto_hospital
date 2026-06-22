package cl.duoc.pacientes_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
		info = @Info(
				title = "Pacientes Service API",
				version = "1.0.0",
				description = """
                        API REST para la gestión de pacientes del sistema hospitalario.

                        Permite:
                        - Registrar pacientes
                        - Consultar pacientes
                        - Actualizar pacientes
                        - Eliminar pacientes
                        """,
				contact = @Contact(
						name = "Martin Jaque",
						email = "ma.jaqueq@duocuc.cl"
				)
		)
)
@SpringBootApplication
@EnableDiscoveryClient
public class PacientesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PacientesServiceApplication.class, args);
	}
}
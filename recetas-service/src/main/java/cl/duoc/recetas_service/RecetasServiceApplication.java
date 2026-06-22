package cl.duoc.recetas_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Recetas Service API",
				version = "1.0.0",
				description = """
                         API REST para la gestión de recetas médicas del sistema hospitalario.

                         Permite:
                         - Emitir recetas
                         - Consultar recetas
                         - Actualizar recetas
                         - Eliminar recetas
                         """,
				contact = @Contact(
						name = "Martin Jaque",
						email = "ma.jaqueq@duocuc.cl"
				)
		)
)
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.duoc.recetas_service.clients")
public class RecetasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecetasServiceApplication.class, args);
	}
}
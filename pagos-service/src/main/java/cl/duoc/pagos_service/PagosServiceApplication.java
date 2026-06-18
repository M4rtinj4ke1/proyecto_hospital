package cl.duoc.pagos_service;

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
				title = "Pagos service API",
				description = "DESCRIPTION " +
						"API para gestionar a los pagos del sistema hospitalario" +
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
@EnableFeignClients(basePackages = "cl.duoc.pagos_service.clients")
public class PagosServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PagosServiceApplication.class, args);
	}
}
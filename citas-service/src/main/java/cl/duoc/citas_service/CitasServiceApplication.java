package cl.duoc.citas_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
		info = @Info(
				title = "Citas service API",
				description = "DESCRIPTION " +
						"API para que la gestion las citas del sistema hospitalario permitiendo" +
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
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class CitasServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CitasServiceApplication.class, args);
	}

}

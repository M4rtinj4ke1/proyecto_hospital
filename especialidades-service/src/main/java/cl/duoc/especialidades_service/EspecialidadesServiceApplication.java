package cl.duoc.especialidades_service;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@OpenAPIDefinition(
		info = @Info(
				title = "Especialidades service API",
				description = "DESCRIPTION " +
						"API para que la gestion de especialidades de los doctores" +
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
@EnableDiscoveryClient
public class EspecialidadesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EspecialidadesServiceApplication.class, args);
	}

}

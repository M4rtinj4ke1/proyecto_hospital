package cl.duoc.historial_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.duoc.historial_service.clients")
public class HistorialServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HistorialServiceApplication.class, args);
	}
}
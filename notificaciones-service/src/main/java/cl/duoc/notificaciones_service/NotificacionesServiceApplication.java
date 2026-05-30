package cl.duoc.notificaciones_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cl.duoc.notificaciones_service.clients")
public class NotificacionesServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(NotificacionesServiceApplication.class, args);
	}
}
package com.exemplo.ingressos;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.exemplo.ingressos.model")
@EnableJpaRepositories(basePackages = "com.exemplo.ingressos.repository")
public class VendaIngressosApplication {
	public static void main(String[] args) {
		SpringApplication.run(VendaIngressosApplication.class, args);
	}
}

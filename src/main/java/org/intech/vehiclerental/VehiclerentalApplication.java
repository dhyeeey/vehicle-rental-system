package org.intech.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "org.intech.vehiclerental.repositories"
)
public class VehiclerentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiclerentalApplication.class, args);
	}

}

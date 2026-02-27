package org.intech.vehiclerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.blazebit.persistence.spring.data.impl.repository.BlazePersistenceRepositoryFactoryBean;

@SpringBootApplication
@EnableJpaRepositories(
		basePackages = "org.intech.vehiclerental.repositories",
		repositoryFactoryBeanClass = BlazePersistenceRepositoryFactoryBean.class
)
public class VehiclerentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiclerentalApplication.class, args);
	}

}

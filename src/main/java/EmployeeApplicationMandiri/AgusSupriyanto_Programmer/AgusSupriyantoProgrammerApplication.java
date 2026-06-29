package EmployeeApplicationMandiri.AgusSupriyanto_Programmer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AgusSupriyantoProgrammerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgusSupriyantoProgrammerApplication.class, args);
	}
    @Bean
    CommandLineRunner test(
            @Value("${DB_URL:NOT_FOUND}") String dbUrl
    ) {
        return args -> System.out.println("DB_URL = " + dbUrl);
    }


}

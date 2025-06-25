package pe.edu.unmsm.sistemas.sistemaparalela;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SistemaDeAdmisionParalelaApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaDeAdmisionParalelaApplication.class, args);
    }

}

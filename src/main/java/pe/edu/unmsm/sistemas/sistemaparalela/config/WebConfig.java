package pe.edu.unmsm.sistemas.sistemaparalela.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") //Aplica la configuración a tus rutas específicas
                .allowedOriginPatterns("*")//Origen permitido(de donde viene el frontend)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")//Métodos HTTP permitidos
                .allowedHeaders("*") //Permite todos los encabezados
                .allowCredentials(false); //Si se necesita permitir credenciales(cookies/tokens)
    }
}

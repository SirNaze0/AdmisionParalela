package pe.edu.unmsm.sistemas.sistemaparalela.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Getter
@Configuration
public class SupabaseConfig {

    @Value("${supabase.url}")
    private String supabaseUrl;
    @Value("${anon.key}")
    private String supabaseKey;
    @Bean
    public RestTemplate restTemplate(){ return new RestTemplate();}
}

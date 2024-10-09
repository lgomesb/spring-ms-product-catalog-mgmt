package com.barbosa.ms.productmgmt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")  // Habilita CORS para todas as rotas
                .allowedOrigins("http://localhost:4200", "http://localhost:4200")  // Permite apenas o React Admin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                .allowedHeaders("*")  // Permite todos os cabeçalhos
                .exposedHeaders("Content-Range", "X-Total-Count")  // Exponha esses cabeçalhos para a resposta ser visível
                .allowCredentials(true);
    }

}

package edu.ifpb.oficina360.config; // Ajuste o pacote se necessário

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;

@Configuration
public class JpaConfiguration {

    // Este Bean garante que o Hibernate carregue a estratégia personalizada.
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer() {
        return properties -> properties.put(
            AvailableSettings.PHYSICAL_NAMING_STRATEGY, 
            "edu.ifpb.oficina360.config.OracleUpperCaseNamingStrategy" // Seu caminho completo
        );
    }
}
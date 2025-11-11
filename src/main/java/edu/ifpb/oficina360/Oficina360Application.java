package edu.ifpb.oficina360; // SEU PACOTE AQUI

import javax.sql.DataSource; // Importe este
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean; // Importe este
import org.springframework.jdbc.datasource.DriverManagerDataSource; // Importe este

@SpringBootApplication 
public class Oficina360Application {

    public static void main(String[] args) {
        SpringApplication.run(Oficina360Application.class, args);
    }

    // -----------------------------------------------------------
    // FORÇA A CONFIGURAÇÃO DO DATASOURCE MANUALMENTE
    // -----------------------------------------------------------
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        
        // Use o driver que está no seu pom.xml
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver"); 
        
        // Suas credenciais
        dataSource.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:xe");
        dataSource.setUsername("system");
        dataSource.setPassword("oficina");
        
        return dataSource;
    }
}
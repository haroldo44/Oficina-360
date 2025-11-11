package edu.ifpb.oficina360.config; 

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import java.io.Serializable;

// Esta é uma implementação mais completa da estratégia
public class OracleUpperCaseNamingStrategy implements PhysicalNamingStrategy, Serializable {

    // Método principal para conversão em MAIÚSCULAS
    private Identifier convertToUpperCase(final Identifier identifier) {
        if (identifier == null || identifier.getText() == null) {
            return identifier;
        }
        final String newName = identifier.getText().toUpperCase();
        return Identifier.toIdentifier(newName);
    }
    
    // Converte Nomes de Colunas (como ID_CLIENTE)
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convertToUpperCase(name);
    }

    // Converte Nomes de Tabelas (como CLIENTE)
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convertToUpperCase(name);
    }

    // Os demais métodos (que controlam aliases e schemas) também devem ser forçados:
    @Override
    public Identifier toPhysicalCatalogName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convertToUpperCase(name);
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convertToUpperCase(name);
    }
    
    // Método que influencia aliases de joins:
    @Override
    public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment jdbcEnvironment) {
        return convertToUpperCase(name);
    }
    
    // O método abaixo não existe na interface principal do Hibernate 6, 
    // mas se for o caso, ele também deve ser forçado.

    // Certifique-se de que a implementação completa (com todos os métodos) esteja no seu código.
}
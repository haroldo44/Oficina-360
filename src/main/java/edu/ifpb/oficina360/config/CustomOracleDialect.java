package edu.ifpb.oficina360.config;

import org.hibernate.dialect.OracleDialect;
import org.hibernate.engine.jdbc.dialect.spi.DialectResolutionInfo;

public class CustomOracleDialect extends OracleDialect {

    // Construtor obrigatório para o Hibernate 6
    public CustomOracleDialect(DialectResolutionInfo resolutionInfo) {
        super(resolutionInfo);
    }
    
    // REMOVA O MÉTODO forceQuote()
    
    // Você pode adicionar este método se precisar de quoting sensível a maiúsculas/minúsculas,
    // mas o ideal é deixar o Spring/Hibernate cuidar disso via properties.
    /*
    @Override
    public IdentifierHelper buildIdentifierHelper(IdentifierHelperBuilder builder, DatabaseMetaData databaseMetaData) throws SQLException {
        // Exemplo: Força o uso do Quoting Strategy. Mantenha isso comentado, a menos que necessário.
        return super.buildIdentifierHelper( builder, databaseMetaData ); 
    }
    */
}
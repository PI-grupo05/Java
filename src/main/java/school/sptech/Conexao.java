package school.sptech;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class Conexao{

    private DataSource conexao;

    public Conexao() {
        BasicDataSource basicDataSource = new BasicDataSource();

        basicDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        basicDataSource.setUrl("jdbc:mysql://localhost:3306/dataryzer?useSSL=false&serverTimezone=UTC");
        basicDataSource.setUsername("root"); // Substitua pelo seu usuário
        basicDataSource.setPassword("Blocounico15!");   // Substitua pela sua senha

        // Configurações opcionais (recomendadas para produção)
        basicDataSource.setInitialSize(5);  // Pool inicial de conexões
        basicDataSource.setMaxTotal(10);    // Máximo de conexões no pool

        this.conexao = basicDataSource;
    }

    public DataSource getConexao() {
        return this.conexao;
    }
}

package school.sptech;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String nomeArquivo = "paraTreinarApache.xlsx";

        // Carregando o arquivo excel
        Path caminho = Path.of(nomeArquivo);
        InputStream arquivo = Files.newInputStream(caminho);

        // Extraindo os livros do arquivo
        LeitorExcel leitorExcel = new LeitorExcel();
        List<Interrupcao> interrupcoes = leitorExcel.extrairInterrupcoes(nomeArquivo, arquivo);

        // Fechando o arquivo após a extração
        arquivo.close();

        System.out.println("Interrupções extraídas:");
        for (Interrupcao interrupcao : interrupcoes) {
            System.out.println(interrupcao);


        }

            Conexao conexao = new Conexao();
            JdbcTemplate template = new JdbcTemplate(conexao.getConexao());

        for (Interrupcao interrupcao : interrupcoes) {
            try {
                Integer count = template.queryForObject(
                        "SELECT COUNT(*) FROM interrupcao WHERE id_interrupcao = ?",
                        Integer.class,
                        interrupcao.getId()
                );

                if (count > 0) {
                    System.out.println("Interrupção com ID " + interrupcao.getId() + " já existe. Pulando inserção.");
                    continue;
                }

                template.update(
                        "INSERT INTO interrupcao (id_interrupcao, dt_inicio, dt_fim, cidade, motivo) VALUES (?, ?, ?, ?, ?)",
                        interrupcao.getId(),
                        interrupcao.getInicio(),
                        interrupcao.getFim(),
                        interrupcao.getUnidadeConsumidora(),
                        interrupcao.getFatorGerador()
                );
                System.out.println("Inserida interrupção ID: " + interrupcao.getId());
            } catch (DataAccessException e) {
                System.err.println("Erro ao inserir interrupção ID " + interrupcao.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("Passei no teste!");
    }


}
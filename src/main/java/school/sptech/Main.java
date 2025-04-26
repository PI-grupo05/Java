package school.sptech;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

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

        // inserção distribuidora

        for (Interrupcao interrupcaoDistro : interrupcoes) {
            try{
                Integer countDistros = template.queryForObject(
                        "SELECT COUNT(*) FROM distribuidora WHERE nome = ?",
                        Integer.class,
                        interrupcaoDistro.getDistribuidora()
                );

                if (countDistros > 0) {
                    continue;
                }

                template.update(
                        "INSERT INTO distribuidora (cnpj,nome,sigla) VALUES (?, ?, ?)",
                        interrupcaoDistro.getCnpj(),
                        interrupcaoDistro.getDistribuidora(),
                        interrupcaoDistro.getSiglaDistro()
                );
            }
            catch (DataAccessException e) {
                System.err.println("Erro ao inserir interrupção ID " + interrupcaoDistro.getId() + ": " + e.getMessage());
            }
        }

        // inserção da cidade

        for (Interrupcao interrupcaoCidade : interrupcoes) {
            try{
                Integer countDistros = template.queryForObject(
                        "SELECT COUNT(*) FROM cidade WHERE nome = ?",
                        Integer.class,
                        interrupcaoCidade.getUnidadeConsumidora()
                );

                if (countDistros > 0) {
                    continue;
                }

                Integer idDistribuidora = template.queryForObject(
                        "SELECT id_distribuidora FROM distribuidora WHERE nome = ?",
                        Integer.class,
                        interrupcaoCidade.getDistribuidora()
                );

                if (idDistribuidora == null) {
                    System.out.println("Distribuidora " + interrupcaoCidade.getDistribuidora() + " não encontrada. Pulando inserção.");
                    continue;
                }


                template.update(
                        "INSERT INTO cidade (nome, fk_distribuidora) VALUES (?, ?)",
                        interrupcaoCidade.getUnidadeConsumidora(),
                        idDistribuidora
                );
            }
            catch (DataAccessException e) {
                System.err.println("Erro ao inserir interrupção ID " + interrupcaoCidade.getId() + ": " + e.getMessage());
            }
        }

        // inserção do motivo

        for (Interrupcao interrupcoeMotivo : interrupcoes) {
            try{
                Integer countfatorGerador = template.queryForObject(
                        "SELECT COUNT(*) FROM motivo WHERE nome = ?",
                        Integer.class,
                        interrupcoeMotivo.getFatorGerador()
                );

                if (countfatorGerador > 0) {
                    continue;
                }

                template.update(
                        "INSERT INTO motivo (nome) VALUES (?)",
                        interrupcoeMotivo.getFatorGerador()
                );
            }
            catch (DataAccessException e) {
                System.err.println("Erro ao inserir interrupção ID " + interrupcoeMotivo.getId() + ": " + e.getMessage());
            }
        }

        // inserção da interrupção

        for (Interrupcao interrupcao : interrupcoes) {
            try {
                Integer countID = template.queryForObject(
                        "SELECT COUNT(*) FROM interrupcao WHERE id_interrupcao = ?",
                        Integer.class,
                        interrupcao.getId()
                );

                if (countID > 0) {
                    continue;
                }

                Integer idCidade = template.queryForObject(
                        "SELECT id_cidade FROM cidade WHERE nome = ?",
                        Integer.class,
                        interrupcao.getUnidadeConsumidora()
                );

                if (idCidade == null) {
                    System.out.println("Cidade " + interrupcao.getUnidadeConsumidora() + " não encontrada. Pulando inserção.");
                    continue;
                }


                Integer idMotivo = template.queryForObject(
                        "SELECT id_motivo FROM motivo WHERE nome = ?",
                        Integer.class,
                        interrupcao.getFatorGerador()
                );

                if (idMotivo == null) {
                    System.out.println("Motivo " + interrupcao.getFatorGerador() + " não encontrado. Pulando inserção.");
                    continue;
                }

                template.update(
                        "INSERT INTO interrupcao (id_interrupcao, dt_inicio, dt_fim, fk_cidade, fk_motivo) VALUES (?, ?, ?, ?, ?)",
                        interrupcao.getId(),
                        interrupcao.getInicio(),
                        interrupcao.getFim(),
                        idCidade,
                        idMotivo
                );
                System.out.println("Inserida interrupção ID: " + interrupcao.getId());
            } catch (DataAccessException e) {
                System.err.println("Erro ao inserir interrupção ID " + interrupcao.getId() + ": " + e.getMessage());
            }
        }

        System.out.println("Passei no teste!");
    }


}
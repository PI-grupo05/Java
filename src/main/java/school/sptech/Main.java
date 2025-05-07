package school.sptech;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String nomeArquivoParaProcurar = "paraTreinarApache.xlsx";
        String nomeBucket = "dataryzer";
        List<Interrupcao> interrupcoes = new ArrayList<Interrupcao>();
        // Ferramenta que fará a leitura
        InputStream arquivo = null;

        //Para acessar a S3
        S3Client s3Client = new S3Provider().getS3Client();

        // Faz a listagem de todos os arquivos no bucket
        try {
            List<S3Object> objects = s3Client.listObjects(ListObjectsRequest.builder().bucket(nomeBucket).build()).contents();
            for (S3Object object : objects) {
                GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                        .bucket(nomeBucket)
                        .key(object.key())
                        .build();

            // Se achar o arquivo, guardar para realizar a leitura
                if(object.key().equals(nomeArquivoParaProcurar)){
                    arquivo = s3Client.getObject(getObjectRequest, ResponseTransformer.toInputStream());
                }
            }
                // Extraindo as interrupções do arquivo
                LeitorExcel leitorExcel = new LeitorExcel();
                interrupcoes = leitorExcel.extrairInterrupcoes(nomeArquivoParaProcurar, arquivo);

                // Fechando o arquivo após a extração
                arquivo.close();

        
        } catch (  S3Exception e) {

            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());

            // Teste de adicionar dados ao logggg

            Conexao conexao = new Conexao();
            JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
            String erroInserir = "Erro ao fazer download dos arquivos: ";
            
            Log log = new Log("ERRO",erroInserir, e.getMessage());

            LogInserir loginserir = new LogInserir(template);
            loginserir.registrarLog(log);
            System.err.println("Erro capturado: " + e.getMessage());


            // FIm do teste de adicionar ao log
        }

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


                // Teste de adicionar dados ao logggg

                String erroInserir = "Erro ao inserir distribuidora";
                Log log = new Log("ERRO",erroInserir, e.getMessage());

                LogInserir loginserir = new LogInserir(template);
                loginserir.registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());


                // FIm do teste de adicionar ao log

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


                // Teste de adicionar dados ao logggg

                String erroInserir = "Erro ao inserir cidade";
                Log log = new Log("ERRO",erroInserir, e.getMessage());

                LogInserir loginserir = new LogInserir(template);
                loginserir.registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());


                // FIm do teste de adicionar ao log
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


                // Teste de adicionar dados ao logggg

                String erroInserir = "Erro ao inserir motivo ";
                Log log = new Log("ERRO",erroInserir, e.getMessage());

                LogInserir loginserir = new LogInserir(template);
                loginserir.registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());


                // FIm do teste de adicionar ao log
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


                // Teste de adicionar dados ao logggg

                String erroInserir = "Erro ao inserir interrupção";
                Log log = new Log("ERRO",erroInserir, e.getMessage());

                LogInserir loginserir = new LogInserir(template);
                loginserir.registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());


                // FIm do teste de adicionar ao log

            }
        }

        System.out.println("Passei no teste!");

    }

}
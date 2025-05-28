package school.sptech.main;

import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.Slack;
import school.sptech.config.Conexao;
import school.sptech.config.S3Provider;
import school.sptech.modulos.Distribuidora;
import school.sptech.modulos.Interrupcao;
import school.sptech.modulos.Log;
import school.sptech.modulos.UnidadeDistribuidora;
import school.sptech.service.LeitorExcel;
import school.sptech.service.LogInserir;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException,InterruptedException{

        String nomeArquivoParaProcurar = "paraTreinarApache.xlsx";
        String nomeBucket = "dataryzer";
        List<Interrupcao> interrupcoes = new ArrayList<Interrupcao>();
        Conexao conexao = new Conexao();
        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
        // Ferramenta que fará a leitura
        InputStream arquivo = null;

        //Para acessar a S3
        S3Client s3Client = new S3Provider().getS3Client();

        //instanciando JSONObject
        JSONObject jsonDistro = new JSONObject();
        JSONObject jsonUnidadeDistribuidora = new JSONObject();
        JSONObject jsonMotivo = new JSONObject();
        JSONObject jsonInterrupcao = new JSONObject();

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

            String informacao = "Exito na extração do arquivo em S3 ";
            Log log = new Log("INFO",informacao, nomeArquivoParaProcurar);

            LogInserir loginserir = new LogInserir(template);
            loginserir.registrarLog(log);


        } catch (  S3Exception e) {

            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());

            // Teste de adicionar dados ao logggg

            String erroInserir = "Erro ao fazer download dos arquivos: ";

            Log log = new Log("ERRO",erroInserir, e.getMessage());

            LogInserir loginserir = new LogInserir(template);
            loginserir.registrarLog(log);
            System.err.println("Erro capturado: " + e.getMessage());


            // Fim do teste de adicionar ao log
        }


        // Codigo para ler localmente e fazer a inserções

//        ==============================================================
//        String nomeArquivoParaProcurar = "paraTreinarApache.xlsx";
//        List<Interrupcao> interrupcoes = new ArrayList<>();
//        Conexao conexao = new Conexao();
//        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
//
//        String caminhoArquivoLocal = "./" + nomeArquivoParaProcurar; // Exemplo: na mesma pasta do projeto
//
//        InputStream arquivo = null;
//
//        try {
//            arquivo = new FileInputStream(caminhoArquivoLocal);
//
//            LeitorExcel leitorExcel = new LeitorExcel();
//            interrupcoes = leitorExcel.extrairInterrupcoes(nomeArquivoParaProcurar, arquivo);
//
//            arquivo.close();
//
//            String informacao = "Exito na extração do arquivo local " + nomeArquivoParaProcurar;
//            Log log = new Log("INFO", informacao, nomeArquivoParaProcurar);
//
//            LogInserir loginserir = new LogInserir(template);
//            loginserir.registrarLog(log);
//
//        } catch (Exception e) {
//            System.err.println("Erro ao ler o arquivo local: " + e.getMessage());
//
//            String erroInserir = "Erro ao ler o arquivo local: ";
//            Log log = new Log("ERRO", erroInserir, e.getMessage());
//
//            LogInserir loginserir = new LogInserir(template);
//            loginserir.registrarLog(log);
//
//            System.err.println("Erro capturado: " + e.getMessage());
//        }
//        ===================================================================
        // fim teste de leitura do arquivoo teste

        System.out.println("Interrupções extraídas:");
        for (Interrupcao interrupcao : interrupcoes) {
            System.out.println(interrupcao);
        }

        // inserção distribuidora
        for (Interrupcao interrupcaoDistro : interrupcoes) {
            try {
                Distribuidora distro = interrupcaoDistro.getUnidadeConsumidora().getDistribuidora();


                Integer countDistros = template.queryForObject(
                        "SELECT COUNT(*) FROM distribuidora WHERE nome = ?",
                        Integer.class,
                        distro.getDistribuidora()
                );

                if (countDistros > 0) continue;

                template.update(
                        "INSERT INTO distribuidora (cnpj, nome, sigla) VALUES (?, ?, ?)",
                        distro.getCnpj(),
                        distro.getDistribuidora(),
                        distro.getSiglaDistro()
                );

                jsonDistro.put("text","Nova distribuidora inserida :red_exclamation_mark:");
                Slack.enviarMensagem(jsonDistro);
            } catch (DataAccessException e) {
                String erroInserir = "Erro ao inserir distribuidora";
                Log log = new Log("ERRO", erroInserir, e.getMessage());
                new LogInserir(template).registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());
            }
        }

        // inserção da cidade
        for (Interrupcao  interrupcaoUnidadeConsumidora : interrupcoes) {
            try {
                UnidadeDistribuidora unidade = interrupcaoUnidadeConsumidora.getUnidadeConsumidora();
                Distribuidora distribuidora = unidade.getDistribuidora();

                Integer countUnidadeConsumidora = template.queryForObject(
                        "SELECT COUNT(*) FROM unidade_consumidora WHERE nome = ?",
                        Integer.class,
                        unidade.getNome()
                );


                if (countUnidadeConsumidora > 0) continue;

                Integer idDistribuidora = template.queryForObject(
                        "SELECT id_distribuidora FROM distribuidora WHERE nome = ?",
                        Integer.class,
                        distribuidora.getDistribuidora()
                );

                if (idDistribuidora == null) {
                    System.out.println("Distribuidora " + distribuidora.getDistribuidora() + " não encontrada. Pulando inserção.");
                    continue;
                }



                template.update(
                        "INSERT INTO unidade_consumidora (nome, fk_distribuidora) VALUES (?, ?)",
                        unidade.getNome(),
                        idDistribuidora
                );

                jsonUnidadeDistribuidora.put("text","Nova unidade consumidora inserida :red_exclamation_mark:");
                Slack.enviarMensagem(jsonUnidadeDistribuidora);
            } catch (DataAccessException e) {
                String erroInserir = "Erro ao inserir unidade cosumidora";
                Log log = new Log("ERRO", erroInserir, e.getMessage());
                new LogInserir(template).registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());
            }
        }

        //Exemplo para unidade_consumidora teste



        // Fim teste para unidade consumidora.....

        // inserção do motivo
        for (Interrupcao interrupcaoMotivo : interrupcoes) {
            try {
                Integer countFator = template.queryForObject(
                        "SELECT COUNT(*) FROM motivo WHERE nome = ?",
                        Integer.class,
                        interrupcaoMotivo.getFatorGerador()
                );

                if (countFator > 0) continue;

                template.update(
                        "INSERT INTO motivo (nome) VALUES (?)",
                        interrupcaoMotivo.getFatorGerador()
                );

                jsonMotivo.put("text","Novo motivo inserido :red_exclamation_mark:");
                Slack.enviarMensagem(jsonMotivo);
            } catch (DataAccessException e) {
                String erroInserir = "Erro ao inserir motivo";
                Log log = new Log("ERRO", erroInserir, e.getMessage());
                new LogInserir(template).registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());
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

                if (countID > 0) continue;

                UnidadeDistribuidora unidade = interrupcao.getUnidadeConsumidora();
                Integer idCidade = template.queryForObject(
                        "SELECT id_unidade_consumidora FROM unidade_consumidora WHERE nome = ?",
                        Integer.class,
                        unidade.getNome()
                );

                if (idCidade == null) {
                    System.out.println("Cidade " + unidade.getNome() + " não encontrada. Pulando inserção.");
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
                        "INSERT INTO interrupcao (id_interrupcao, dt_inicio, dt_fim, fk_unidade_consumidora, fk_motivo) VALUES (?, ?, ?, ?, ?)",
                        interrupcao.getId(),
                        interrupcao.getInicio(),
                        interrupcao.getFim(),
                        idCidade,
                        idMotivo
                );

                System.out.println("Inserida interrupção ID: " + interrupcao.getId());

                jsonInterrupcao.put("text","Nova interrupção inserida :red_exclamation_mark:");
                Slack.enviarMensagem(jsonInterrupcao);
            } catch (DataAccessException e) {
                String erroInserir = "Erro ao inserir interrupção";
                Log log = new Log("ERRO", erroInserir, e.getMessage());
                new LogInserir(template).registrarLog(log);
                System.err.println("Erro capturado: " + e.getMessage());
            }
        }

        System.out.println("Passei no teste!");
    }
}

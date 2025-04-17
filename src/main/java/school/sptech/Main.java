package school.sptech;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String nomeArquivoParaProcurar = "paraTreinarApache.xlsx";
        String nomeBucket = "bucket-dataryzer";

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
                List<Interrupcao> interrupcoes = leitorExcel.extrairInterrupcoes(nomeArquivoParaProcurar, arquivo);

                // Fechando o arquivo após a extração
                arquivo.close();

                // Exibe as interrupções armazenadas no objeto
                System.out.println("Interrupções extraídas:");
                for (Interrupcao interrupcao : interrupcoes) {
                    System.out.println(interrupcao);
                }
        } catch (  S3Exception e) {
            System.err.println("Erro ao fazer download dos arquivos: " + e.getMessage());
        }

    }
}
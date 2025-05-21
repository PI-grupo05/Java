package school.sptech.config;

import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class S3Provider {

    public S3Client getS3Client(){
        return S3Client.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

//    private final AwsSessionCredentials credentials;
//
//    // Credenciais para acessar o S3
//    // Chave de acesso
//    // Chave secreta
//    // Token
//    public S3Provider() {
//        this.credentials = AwsSessionCredentials.create(
//                System.getenv("AWS_ACCESS_KEY_ID"),
//                System.getenv("AWS_SECRET_ACCESS_KEY"),
//                System.getenv("AWS_SESSION_TOKEN")
//        );
//    }
//
//    // Função para realizar o acesso com base na credenciais
//    public S3Client getS3Client() {
//        return S3Client.builder()
//                .region(Region.US_EAST_1)
//                .credentialsProvider(() -> credentials)
//                .build();
//    }
}


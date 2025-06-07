package school.sptech.modulos;

import org.json.JSONObject;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.config.Conexao;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;

public class Parametrizacao {
    private Integer id;
    private String url;
    private Boolean receberNotificacao;
    private String frequenciaNotificacao;
    private LocalDate proximaNotificacao;
    private Integer fk_distribuidora;

    public Parametrizacao() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getReceberNotificacao() {
        return receberNotificacao;
    }

    public void setReceberNotificacao(Boolean receberNotificacao) {
        this.receberNotificacao = receberNotificacao;
    }

    public String getFrequenciaNotificacao() {
        return frequenciaNotificacao;
    }

    public void setFrequenciaNotificacao(String frequenciaNotificacao) {
        this.frequenciaNotificacao = frequenciaNotificacao;
    }

    public LocalDate getProximaNotificacao() {
        return proximaNotificacao;
    }

    public void setProximaNotificacao(LocalDate proximaNotificacao) {
        this.proximaNotificacao = proximaNotificacao;
    }

    public Integer getFk_distribuidora() {
        return fk_distribuidora;
    }

    public void setFk_distribuidora(Integer fk_distribuidora) {
        this.fk_distribuidora = fk_distribuidora;
    }

    @Override
    public String toString() {
        return "Parametrizacao{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", receberNotificacao=" + receberNotificacao +
                ", frequenciaNotificacao='" + frequenciaNotificacao + '\'' +
                ", proximaNotificacao=" + proximaNotificacao +
                ", fk_distribuidora=" + fk_distribuidora +
                '}';
    }

    public static List<Parametrizacao> pegarParametrizacoes(){
        Conexao conexao = new Conexao();
        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());

        return template.query("SELECT * FROM parametrizacao",
                new BeanPropertyRowMapper<>(Parametrizacao.class));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println(Parametrizacao.pegarParametrizacoes());
    }

    private static HttpClient client = HttpClient.newHttpClient();

    public static void enviarMensagem(List<DistribuidoraNotificacao> distribuidoraNotificacoes) throws IOException, InterruptedException {
        List<Parametrizacao> parametrizacoes = Parametrizacao.pegarParametrizacoes();
        for (Parametrizacao parametrizacao : parametrizacoes) {
            if(parametrizacao.receberNotificacao.equals(false)){
                continue;
            }
            else if(parametrizacao.proximaNotificacao.equals(LocalDate.now()) ||
            parametrizacao.proximaNotificacao.isBefore(LocalDate.now())){
                for (DistribuidoraNotificacao distribuidoraNotificacao : distribuidoraNotificacoes) {
                    if(parametrizacao.fk_distribuidora.equals(distribuidoraNotificacao.getDistribuidora().getIdDistribuidora())){
                        String mensagem = "";

                        if(distribuidoraNotificacao.getTeveNovaInterrupcao().equals(true)){
                            mensagem += "Nova interrupção inserida\n";
                        }

                        if(distribuidoraNotificacao.getTeveNovaUnidadeConsumidora().equals(true)){
                            mensagem += "Nova unidade consumidora inserida ✅\n";
                        }

                        if(distribuidoraNotificacao.getTeveNovoMotivo().equals(true)){
                            mensagem += "Novo motivo inserido❗\n";
                        }

                        if(mensagem.isBlank()){
                            mensagem = "Não há novos registros";
                        }

                        JSONObject json = new JSONObject();
                        json.put("text", mensagem);
                        System.out.println(json.toString());

                        HttpRequest request = HttpRequest.newBuilder(
                                URI.create(parametrizacao.url))
                                .header("accept","application/json")
                                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                                .build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        System.out.println(String.format("Status: %s", response.statusCode()));
                        System.out.println(String.format("Response: %s", response.body()));

                        LocalDate diaDefinido = parametrizacao.proximaNotificacao;
                        if(parametrizacao.frequenciaNotificacao.equals("Diário")){
                            parametrizacao.proximaNotificacao = diaDefinido.plusDays(1);
                        }
                        else if(parametrizacao.frequenciaNotificacao.equals("Semanal")){
                            parametrizacao.proximaNotificacao = diaDefinido.plusWeeks(1);
                        }
                        else{
                            parametrizacao.proximaNotificacao = diaDefinido.plusMonths(1);
                        }

                        Conexao conexao = new Conexao();
                        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
                        template.update("UPDATE parametrizacao set proxima_notificacao = ? " +
                                        "where fk_distribuidora = ?",
                                parametrizacao.proximaNotificacao,
                                parametrizacao.fk_distribuidora);

                    }
                }
            }

        }


    }
}

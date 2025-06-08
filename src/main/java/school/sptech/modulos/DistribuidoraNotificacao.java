package school.sptech.modulos;

import org.springframework.jdbc.core.JdbcTemplate;
import school.sptech.config.Conexao;

import java.util.ArrayList;
import java.util.List;

public class DistribuidoraNotificacao {
    private Boolean teveNovaInterrupcao;
    private Boolean teveNovaUnidadeConsumidora;
    private Distribuidora distribuidora;

    public DistribuidoraNotificacao() {
    }

    public DistribuidoraNotificacao(Distribuidora distribuidora) {
        this.teveNovaInterrupcao = false;
        this.teveNovaUnidadeConsumidora = false;
        this.distribuidora = distribuidora;
    }

    public Boolean getTeveNovaInterrupcao() {
        return teveNovaInterrupcao;
    }

    public void setTeveNovaInterrupcao(Boolean teveNovaInterrupcao) {
        this.teveNovaInterrupcao = teveNovaInterrupcao;
    }

    public Boolean getTeveNovaUnidadeConsumidora() {
        return teveNovaUnidadeConsumidora;
    }

    public void setTeveNovaUnidadeConsumidora(Boolean teveNovaUnidadeConsumidora) {
        this.teveNovaUnidadeConsumidora = teveNovaUnidadeConsumidora;
    }

    public Distribuidora getDistribuidora() {
        return distribuidora;
    }

    public void setDistribuidora(Distribuidora distribuidora) {
        this.distribuidora = distribuidora;
    }

    @Override
    public String toString() {
        return "DistribuidoraNotificacao{" +
                "teveNovaInterrupcao=" + teveNovaInterrupcao +
                ", teveNovaUnidadeConsumidora=" + teveNovaUnidadeConsumidora +
                ", distribuidora=" + distribuidora +
                '}';
    }

    public List<DistribuidoraNotificacao> pegarDistribuidorasExistentes(){
        Conexao conexao = new Conexao();
        JdbcTemplate template = new JdbcTemplate(conexao.getConexao());
        List<DistribuidoraNotificacao> notificacoes = new ArrayList<>();
        template.query("select id_distribuidora, nome from distribuidora",
                rs -> {notificacoes.add(
                        new DistribuidoraNotificacao(
                                new Distribuidora(rs.getInt("id_distribuidora"),
                                        rs.getString("nome")
                                        )));
        });
        return notificacoes;
    }
}

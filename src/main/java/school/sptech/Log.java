package school.sptech;

import java.time.LocalDateTime;

public class Log {


    private Integer idLog;
    private LocalDateTime dataHora;
    private String nivel;
    private String mensagem;
    private String mensagemLog;


    public Log(String nivel, String mensagem, String mensagemLog) {
        this.nivel = nivel;
        this.mensagem = mensagem;
        this.mensagemLog = mensagemLog;
    }


    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public String getMensagemLog() {
        return mensagemLog;
    }

    public void setMensagemLog(String mensagemLog) {
        this.mensagemLog = mensagemLog;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}


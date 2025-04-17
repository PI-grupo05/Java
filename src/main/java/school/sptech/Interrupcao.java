package school.sptech;

import java.time.LocalDateTime;

public class Interrupcao {
    private Integer id;
    private String unidadeConsumidora;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String fatorGerador;
    private Long duracao;

    public Interrupcao() {
    }

    public Interrupcao(Integer id, String unidadeConsumidora, LocalDateTime inicio, LocalDateTime fim, String fatorGerador, Long duracao) {
        this.id = id;
        this.unidadeConsumidora = unidadeConsumidora;
        this.inicio = inicio;
        this.fim = fim;
        this.fatorGerador = fatorGerador;
        this.duracao = duracao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(String unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }

    public String getFatorGerador() {
        return fatorGerador;
    }

    public void setFatorGerador(String fatorGerador) {
        this.fatorGerador = fatorGerador;
    }

    public Long getDuracao() {
        return duracao;
    }

    public void setDuracao(Long duracao) {
        this.duracao = duracao;
    }

    @Override
    public String toString() {
        return "Interrupcao{" +
                "id=" + id +
                ", unidadeConsumidora='" + unidadeConsumidora + '\'' +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", fatorGerador='" + fatorGerador + '\'' +
                ", duracao=" + duracao +
                '}';
    }
}

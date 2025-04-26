package school.sptech;

import java.time.LocalDateTime;

public class Interrupcao {
    private Integer id;
    private String unidadeConsumidora;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String fatorGerador;
    private String distribuidora;
    private String cnpj;
    private String siglaDistro;

    public Interrupcao() {
    }

    public Interrupcao(Integer id, String unidadeConsumidora, LocalDateTime inicio, LocalDateTime fim, String fatorGerador, String distribuidora ,
                       String siglaDistro,String cnpj) {
        this.id = id;
        this.unidadeConsumidora = unidadeConsumidora;
        this.inicio = inicio;
        this.fim = fim;
        this.fatorGerador = fatorGerador;
        this.distribuidora = distribuidora;
        this.siglaDistro = siglaDistro;
        this.cnpj = cnpj;
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

    public String getDistribuidora() {
        return distribuidora;
    }

    public void setDistribuidora(String distribuidora) {
        this.distribuidora = distribuidora;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSiglaDistro() {
        return siglaDistro;
    }

    public void setSiglaDistro(String siglaDistro) {
        this.siglaDistro = siglaDistro;
    }

    @Override
    public String toString() {
        return "Interrupcao{" +
                "id=" + id +
                ", unidadeConsumidora='" + unidadeConsumidora + '\'' +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", fatorGerador='" + fatorGerador + '\'' +
                ", distribuidora='" + distribuidora + '\'' +
                ", siglaDistro='" + siglaDistro + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}

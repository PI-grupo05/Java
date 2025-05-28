package school.sptech.modulos;

import java.time.LocalDateTime;

public class Interrupcao {
    private Integer id;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private String fatorGerador;

    private UnidadeDistribuidora unidadeConsumidora; // ✅ Faltava isso

    public Interrupcao() {
    }

    public Interrupcao(Integer id, LocalDateTime inicio, LocalDateTime fim, String fatorGerador, UnidadeDistribuidora unidadeConsumidora) {
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.fatorGerador = fatorGerador;
        this.unidadeConsumidora = unidadeConsumidora;
    }

//    public Interrupcao(Integer id, UnidadeDistribuidora unidadeConsumidora, LocalDateTime inicio, LocalDateTime fim, String fatorGerador) {
//        this.id = id;
//        this.unidadeConsumidora = unidadeConsumidora;
//        this.inicio = inicio;
//        this.fim = fim;
//        this.fatorGerador = fatorGerador;
//    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public UnidadeDistribuidora getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(UnidadeDistribuidora unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    @Override
    public String toString() {
        return "Interrupcao{" +
                "id=" + id +
                ", unidadeConsumidora=" + (unidadeConsumidora != null ? unidadeConsumidora.getNome() : "null") +
                ", inicio=" + inicio +
                ", fim=" + fim +
                ", fatorGerador='" + fatorGerador + '\'' +
                '}';
    }


}

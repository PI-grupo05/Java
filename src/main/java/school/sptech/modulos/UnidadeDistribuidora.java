package school.sptech.modulos;

public class UnidadeDistribuidora {
    private Integer id;
    private String nome;
    private Distribuidora distribuidora;

    public UnidadeDistribuidora() {}

    public UnidadeDistribuidora(String nome, Distribuidora distribuidora) {
        this.nome = nome;
        this.distribuidora = distribuidora;
    }

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public Distribuidora getDistribuidora() { return distribuidora; }
    public void setDistribuidora(Distribuidora distribuidora) { this.distribuidora = distribuidora; }
}
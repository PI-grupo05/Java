package school.sptech.modulos;

public class Distribuidora {
    private Integer idDistribuidora;
    private String nomeDistribuidora;
    private String cnpj;
    private String siglaDistro;

    public Distribuidora(String cnpj, String distribuidora, String siglaDistro) {
        this.cnpj = cnpj;
        this.nomeDistribuidora = distribuidora;
        this.siglaDistro = siglaDistro;
    }

    public Distribuidora(Integer idDistribuidora, String distribuidora) {
        this.idDistribuidora = idDistribuidora;
        this.nomeDistribuidora = distribuidora;
    }

    public Integer getIdDistribuidora() {
        return idDistribuidora;
    }

    public void setIdDistribuidora(Integer idDistribuidora) {
        this.idDistribuidora = idDistribuidora;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getNomeDistribuidora() {
        return nomeDistribuidora;
    }


    public void setNomeDistribuidora(String nomeDistribuidora) {
        this.nomeDistribuidora = nomeDistribuidora;
    }

    public String getSiglaDistro() {
        return siglaDistro;
    }

    public void setSiglaDistro(String siglaDistro) {
        this.siglaDistro = siglaDistro;
    }

    @Override
    public String toString() {
        return "Distribuidora{" +
                "idDistribuidora=" + idDistribuidora +
                ", distribuidora='" + nomeDistribuidora + '\'' +
                '}';
    }
}

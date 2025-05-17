package school.sptech.modulos;

public class Distribuidora {
    private String distribuidora;
    private String cnpj;
    private String siglaDistro;

    public Distribuidora(String cnpj, String distribuidora, String siglaDistro) {
        this.cnpj = cnpj;
        this.distribuidora = distribuidora;
        this.siglaDistro = siglaDistro;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDistribuidora() {
        return distribuidora;
    }


    public void setDistribuidora(String distribuidora) {
        this.distribuidora = distribuidora;
    }

    public String getSiglaDistro() {
        return siglaDistro;
    }

    public void setSiglaDistro(String siglaDistro) {
        this.siglaDistro = siglaDistro;
    }
}

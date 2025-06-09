package school.sptech.modulos;

public abstract class EventoUnidade {
    private UnidadeDistribuidora unidadeConsumidora;

    public EventoUnidade(UnidadeDistribuidora unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }

    public UnidadeDistribuidora getUnidadeConsumidora() {
        return unidadeConsumidora;
    }

    public void setUnidadeConsumidora(UnidadeDistribuidora unidadeConsumidora) {
        this.unidadeConsumidora = unidadeConsumidora;
    }
}

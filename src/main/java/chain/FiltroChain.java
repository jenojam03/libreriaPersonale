package chain;

import model.Libro;

import java.util.List;

public abstract class FiltroChain implements Filtro{

    protected Filtro next;

    public Filtro setNext(Filtro next) {
        this.next = next;
        return next;
    }

    @Override
    public List<Libro> filtra(List<Libro> input) {
        List<Libro> filtrati = applica(input);
        if (next != null) {
            return next.filtra(filtrati);
        }
        return filtrati;
    }

    protected abstract List<Libro> applica(List<Libro> input);


}


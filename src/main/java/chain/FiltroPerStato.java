package chain;

import model.Libro;
import model.StatoLettura;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerStato extends FiltroChain {
    private StatoLettura stato;

    public FiltroPerStato(StatoLettura stato) {
        this.stato = stato;
    }

    @Override
    protected List<Libro> applica(List<Libro> libriDaFiltrare) {
        List<Libro> ret = new ArrayList<>();
        for (Libro l : libriDaFiltrare) {
            if (stato == null || l.getStatoLettura().equals(stato))
                ret.add(l);

        }

        return ret;
    }
}

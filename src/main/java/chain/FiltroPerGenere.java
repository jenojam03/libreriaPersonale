package chain;

import model.Genere;
import model.Libro;

import java.util.ArrayList;
import java.util.List;

public class FiltroPerGenere extends FiltroChain {
    private Genere genere;

    public FiltroPerGenere(Genere genere) {
        this.genere = genere;
    }

    @Override
    protected List<Libro> applica(List<Libro> libriDaFiltrare) {
        List<Libro> ret = new ArrayList<>();
        for (Libro l : libriDaFiltrare) {
            if (genere == null || l.getGenere().equals(genere))
                ret.add(l);

        }

        return ret;
    }


}

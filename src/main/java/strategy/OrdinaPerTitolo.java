package strategy;

import model.Libro;

import java.util.List;

public class OrdinaPerTitolo implements OrdinamentoStrategy {


    @Override
    public void ordina(List<Libro> libri, boolean crescente) {
        if(crescente)
            libri.sort((l1, l2) -> l1.getTitolo().compareToIgnoreCase(l2.getTitolo()));
        else
            libri.sort((l1, l2) -> l2.getTitolo().compareToIgnoreCase(l1.getTitolo()));
    }
}


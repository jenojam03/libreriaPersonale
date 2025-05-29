package strategy;

import model.Libro;

import java.util.List;

public class OrdinaPerAutore implements OrdinamentoStrategy{

    @Override
    public void ordina(List<Libro> libri, boolean crescente) {
        if(crescente)
            libri.sort((l1, l2) -> l1.getAutore().compareToIgnoreCase(l2.getAutore()));
        else
            libri.sort((l1, l2) -> l2.getAutore().compareToIgnoreCase(l1.getAutore()));
    }
}

package strategy;

import model.Libro;

import java.util.List;

public class OrdinaPerValutazione implements OrdinamentoStrategy{
    @Override
    public void ordina(List<Libro> libri, boolean crescente) {
            libri.sort((l1, l2) -> {
                if (crescente)
                    return l1.getValutazione() > l2.getValutazione() ? 1 : -1;
                else
                    return l1.getValutazione() < l2.getValutazione() ? 1 : -1;
            });
    }
}

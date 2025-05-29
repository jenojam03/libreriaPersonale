package strategy;

import model.Libro;

import java.util.List;

public interface OrdinamentoStrategy {
    void ordina(List<Libro> libri, boolean crescente);

}

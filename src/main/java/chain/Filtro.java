package chain;

import model.Libro;

import java.util.List;

public interface Filtro {

    List<Libro> filtra(List<Libro> input);
    Filtro setNext(Filtro next);
}

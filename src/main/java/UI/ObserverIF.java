package UI;

import model.Libro;

import java.util.Map;

public interface ObserverIF {

    void update(Map<String, Libro> libri);
}

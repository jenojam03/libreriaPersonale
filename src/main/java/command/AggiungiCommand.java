package command;

import model.Libreria;
import model.Libro;

public class AggiungiCommand implements Command {

    private Libreria libreria;
    private Libro libro;

    public AggiungiCommand(Libreria libreria, Libro libro) {
        this.libreria = libreria;
        this.libro = libro;
    }

    @Override
    public boolean execute() {
        return libreria.aggiungiLibro(libro);
    }

    @Override
    public boolean undo() {
        libreria.rimuoviLibro(libro.getISBN());

        return true;
    }

}

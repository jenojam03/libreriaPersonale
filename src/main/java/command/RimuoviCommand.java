package command;

import model.Libreria;
import model.Libro;

public class RimuoviCommand implements Command{
    private Libreria libreria;
    private String isbn; //id del libro da rimuovere
    private Libro daRimuovere;

    public RimuoviCommand(Libreria libreria, String isbn) {
        this.libreria = libreria;
        this.isbn = isbn;
    }

    @Override
    public boolean execute() {
        daRimuovere = libreria.getLibro(isbn);
        //controlla se il libro da rimuovere esiste
        if (daRimuovere != null) {
            libreria.rimuoviLibro(isbn);
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        //se l'ho rimosso lo aggiungo
        if (daRimuovere != null) {
            libreria.aggiungiLibro(daRimuovere);
            return true;
        }
        return false;
    }


}

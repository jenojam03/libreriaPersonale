package command;

import model.Libreria;
import model.Libro;

public class ModificaCommand implements Command{
    private Libreria libreria;
    private String isbn; //id libro da modificare
    private Libro nuovoLibro;
    private Libro vecchioLibro; //riferimento alla versione vecchia

    public ModificaCommand(Libreria libreria, String isbn, Libro nuovoLibro) {
        this.libreria = libreria;
        this.isbn = isbn;
        this.nuovoLibro = nuovoLibro;
    }

    @Override
    public boolean execute() {
        Libro originale = libreria.getLibro(isbn);
        if (originale != null) {
            //deep copy del vecchio libro
            vecchioLibro = new Libro(
                    originale.getTitolo(),
                    originale.getAutore(),
                    originale.getISBN(),
                    originale.getGenere(),
                    originale.getStatoLettura(),
                    originale.getValutazione()
            );
            libreria.modificaLibro(isbn, nuovoLibro);
            return true;
        }
        return false;
    }

    @Override
    public boolean undo() {
        if (vecchioLibro != null) {
            libreria.modificaLibro(isbn, vecchioLibro);
            return true;
        }
        return false;
    }


}

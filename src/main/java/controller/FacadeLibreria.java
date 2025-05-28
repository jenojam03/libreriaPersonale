package controller;

import command.AggiungiCommand;
import command.HistoryCommandHandler;
import command.ModificaCommand;
import command.RimuoviCommand;
import model.*;
import persistence.JsonStorageManager;
import java.io.IOException;
import java.util.*;

public class FacadeLibreria {

        private final Libreria libreria = new Libreria();
        private final HistoryCommandHandler handler = new HistoryCommandHandler();

        public FacadeLibreria() throws IOException {
            Map<String,Libro> libri = JsonStorageManager.caricamento();
            if (libri != null) {
                libreria.setLibri(libri);
            }
        }

        public Libro getLibroByISBN(String ISBN){
            return libreria.getLibro(ISBN);
        }

        public Libreria getLibreria() {
            return libreria;
        }

        public boolean aggiungiLibro(Libro libro) throws IOException {
            boolean ret = handler.handle(new AggiungiCommand(libreria, libro));
            salva();
            return ret;
        }

        public void rimuoviLibro(String isbn) throws IOException {
            handler.handle(new RimuoviCommand(libreria, isbn));
            salva();
        }

        public void modificaLibro(String isbn, Libro libroModificato) throws IOException {
            handler.handle(new ModificaCommand(libreria, isbn, libroModificato));
            salva();
        }

        public void undo() throws IOException {
            handler.undo();
            salva();
        }

        public void redo() throws IOException {
            handler.redo();
            salva();
        }

        public Map<String,Libro> getLibri() {
            return libreria.getTuttiILibri();
        }

        private void salva() throws IOException {
            JsonStorageManager.salvataggio(libreria.getTuttiILibri());
        }


    //metodo di ordinamento
    public List<Libro> ordina(CriterioOrdinamento criterio, boolean crescente, List<Libro> libri) {
        switch (criterio) {
            case TITOLO: return libreria.ordinaPerTitolo(libri, crescente);
            case AUTORE: return libreria.ordinaPerAutore(libri, crescente);
            case VALUTAZIONE: return libreria.ordinaPerValutazione(libri, crescente);
            default: return new ArrayList<>();
        }
    }


    //metodo di filtraggio
    public List<Libro> filtra(Genere genere, StatoLettura stato, List<Libro> daFiltrare){
            return libreria.filtra(genere, stato, daFiltrare);
    }

    //metodo di ricerca: restituisce tutto ciò che matcha ciò che è stato inserito
    public List<Libro> ricerca(String criterio){
        Set<Libro> setTemporaneo = new HashSet<>(libreria.cercaPerTitolo(criterio));
        setTemporaneo.addAll(libreria.cercaPerAutore(criterio));
        Libro libroISBN = libreria.cercaPerISBN(criterio);
        if (libroISBN != null) {
            setTemporaneo.add(libroISBN);
        }
        return new ArrayList<>(setTemporaneo);

    }


}

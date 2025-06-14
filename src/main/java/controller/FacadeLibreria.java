package controller;

import command.AggiungiCommand;
import command.HistoryCommandHandler;
import command.ModificaCommand;
import command.RimuoviCommand;
import model.*;
import persistence.JsonStorageManager;
import strategy.OrdinamentoStrategy;
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

        public Map<String,Libro> getLibri() {
            return libreria.getTuttiILibri();
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


        private void salva() throws IOException {
            JsonStorageManager.salvataggio(getLibri());
        }


        //metodo di ordinamento
        public void ordina(OrdinamentoStrategy strategia, boolean crescente) {
            List<Libro> libri = getDaVisualizzare();
            libreria.ordina(strategia, libri, crescente);
        }


        //metodo di filtraggio
        public void filtra(Genere genere, StatoLettura stato){
            List<Libro> daFiltrare;
            if(libreria.isRicerca())
                daFiltrare = libreria.getRisultatiRicerca();
            else
                daFiltrare = new ArrayList<>(libreria.getTuttiILibri().values());
            libreria.filtra(genere, stato,daFiltrare);
        }

        //metodo di ricerca: restituisce tutto ciò che matcha ciò che è stato inserito
        public void ricerca(String criterio){
            libreria.ricerca( criterio );

        }

        public void mostraTutti(){
            libreria.mostraTutti();
        }

        public List<Libro> getDaVisualizzare(){
            return libreria.getDaVisualizzare();
        }


        public boolean isFiltroAttivo(){
            return libreria.isFiltro();
        }

        public Genere getFiltroGenere(){
            return libreria.getFiltroGenere();
        }

        public StatoLettura getFiltroStatoLettura(){
            return libreria.getFiltroStato();
        }

        public String getParolaChiave(){
            return libreria.getParola();
        }

        public void setRicerca(boolean ricerca){
            libreria.setRicerca(ricerca);
        }

        public void setFiltroAttivo(boolean filtro){
            libreria.setFiltro(filtro);
        }

        public void setFiltroGenere(Genere genere){
            libreria.setFiltroGenere(genere);
        }

        public void setFiltroStato(StatoLettura stato){
            libreria.setFiltroStato(stato);
        }

        public void setParola(String parola){
            libreria.setParola(parola);
        }

}

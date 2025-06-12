package model;
import UI.*;
import chain.Filtro;
import chain.FiltroPerGenere;
import chain.FiltroPerStato;
import strategy.OrdinamentoStrategy;

import java.util.*;


//classe che rappresenta il sistema
public class Libreria {


    private Map<String, Libro> libri = new HashMap<>(); //archivio di tutti i libri
    private List<ObserverIF> observers = new ArrayList<>(); //supporto al pattern Observer
    private List<Libro> daVisualizzare = new ArrayList<>(libri.values()); //vista da fornire alla GUI



    public Libro getLibro(String isbn) {
        return libri.get(isbn);
    }

    public Map<String,Libro> getTuttiILibri() {
        return libri;
    }

    public boolean aggiungiLibro(Libro libro) {
        //prevenzione duplicati
        if(libri.containsKey(libro.getISBN())){
            return false;
        }
        libri.put(libro.getISBN(), libro);
        daVisualizzare.add(libro);
        notifyObservers();
        return true;
    }

    public void rimuoviLibro(String isbn) {
        Libro l = libri.remove(isbn);
        daVisualizzare.remove(l);
        notifyObservers();
    }

    public void modificaLibro(String isbn, Libro libroAggiornato) {
        libri.put(isbn, libroAggiornato);
        int index = -1;
        for(Libro l : daVisualizzare){
            if(l.getISBN().equals(isbn)) {
                index = daVisualizzare.indexOf(l);
            }
        }
        daVisualizzare.set(index, libroAggiornato);
        notifyObservers();
    }



    public void mostraTutti(){
        daVisualizzare = new ArrayList<>(libri.values());
        notifyObservers();
    }

    public List<Libro> getDaVisualizzare() {
        return daVisualizzare;
    }

    public void setLibri(Map<String, Libro> libris) {
        libri.clear();
        libri.putAll(libris);
        notifyObservers();
    }


    //RICERCA
    public void ricerca(String criterio) {
        Set<Libro> setTemporaneo = new HashSet<>();

        //cerco in base al titolo e autore
        for (Libro l : libri.values()) {
            if (l.getTitolo().toLowerCase().contains(criterio.trim().toLowerCase()) ||
                    l.getAutore().toLowerCase().contains(criterio.trim().toLowerCase())) {
                setTemporaneo.add(l);
            }
        }


        //cerco per isbn
        Libro l = libri.getOrDefault(criterio, null);
        if(l != null) {
            setTemporaneo.add(l);
        }


        daVisualizzare = new ArrayList<>(setTemporaneo);
        notifyObservers();

    }


    //FILTRAGGIO
    public void filtra(Genere genere, StatoLettura stato, List<Libro> libriDaFiltrare) {

        Filtro filtro = new FiltroPerGenere(genere);
        filtro.setNext(new FiltroPerStato(stato));
        daVisualizzare = filtro.filtra(libriDaFiltrare);
        notifyObservers();

    }



    //ORDINAMENTO
    public void ordina(OrdinamentoStrategy strategia, List<Libro> libri, boolean crescente) {
        strategia.ordina(libri, crescente);
        daVisualizzare = libri;
        notifyObservers();
    }


    //METODI PER OBSERVER
    public void attach(ObserverIF o){
        observers.add(o);
    }

    public void detach(ObserverIF o){
        observers.remove(o);
    }

    public void notifyObservers(){
        for(ObserverIF o : observers){
            o.update();
        }
    }
}
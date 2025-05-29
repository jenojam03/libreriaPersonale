package model;
import UI.*;
import chain.Filtro;
import chain.FiltroPerGenere;
import chain.FiltroPerStato;
import strategy.OrdinamentoStrategy;

import java.util.*;


//classe che rappresenta il sistema
public class Libreria {

    private static Libreria instance;

    private Map<String, Libro> libri = new HashMap<>(); //archivio di tutti i libri
    private List<ObserverIF> observers = new ArrayList<>();
    private List<Libro> daVisualizzare = new ArrayList<>(libri.values()); //vista

    private Libreria() {}

    public synchronized static Libreria getInstance() {
        if (instance == null) {
            instance = new Libreria();
        }
        return instance;
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

    public Libro getLibro(String isbn) {
        return libri.get(isbn);
    }

    public Map<String,Libro> getTuttiILibri() {
        return libri;
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

    //RICERCA E FILTRAGGIO

    /*
    public void cercaPerTitolo(String titolo){
        List<Libro> ret = new ArrayList<>();
        if(titolo == null){
            return;
        }

        for(Libro l : libri.values()){
            if(l.getTitolo().toLowerCase().contains(titolo.trim().toLowerCase())){
                ret.add(l);
            }
        }
        daVisualizzare = ret;
        notifyObservers();

    }

    public void cercaPerAutore(String autore){
        List<Libro> ret = new ArrayList<>();
        if(autore == null){
            return;
        }

        for(Libro l : libri.values()){
            if(l.getAutore().toLowerCase().contains(autore.trim().toLowerCase())){
                ret.add(l);
            }
        }
        daVisualizzare = ret;
        notifyObservers();
    }


    public void cercaPerISBN(String isbn){
        daVisualizzare = new ArrayList<>();
        daVisualizzare.add(libri.get(isbn));
        notifyObservers();
    }*/

    public void ricerca(String criterio) {
        Set<Libro> setTemporaneo = new HashSet<>();

        //cerco in base al titolo
        for (Libro l : libri.values()) {
            if (l.getTitolo().toLowerCase().contains(criterio.trim().toLowerCase())) {
                setTemporaneo.add(l);
            }
        }

        //cerco per autore
        for (Libro l : libri.values()) {
            if (l.getAutore().toLowerCase().contains(criterio.trim().toLowerCase())) {
                setTemporaneo.add(l);
            }
        }


        //cerca per isbn
        Libro l = libri.getOrDefault(criterio, null);
        if(l != null) {
            setTemporaneo.add(l);
        }


        daVisualizzare = new ArrayList<>(setTemporaneo);
        notifyObservers();

    }



    /*
        setTemporaneo.addAll(libreria.cercaPerAutore(criterio));
        Libro libroISBN = libreria.cercaPerISBN(criterio);
        if (libroISBN != null) {
            setTemporaneo.add(libroISBN);
        }
        return new ArrayList<>(setTemporaneo);
    }*/




    public void filtra(Genere genere, StatoLettura stato, List<Libro> libriDaFiltrare) {
        /*List<Libro> ret = new ArrayList<>();

        for (Libro l : libriDaFiltrare) {
            boolean matchGenere = (genere == null || l.getGenere().equals(genere));
            boolean matchStato = (stato == null || l.getStatoLettura().equals(stato));

            if (matchGenere && matchStato) {
                ret.add(l);
            }
        }*/
        Filtro filtro = new FiltroPerGenere(genere);
        filtro.setNext(new FiltroPerStato(stato));
        daVisualizzare = filtro.filtra(libriDaFiltrare);
        notifyObservers();

    }
        /*List<Libro> ret = new ArrayList<>();
        for(Libro l : libriDaFiltrare){
            if(l.getStatoLettura().equals(stato) && l.getGenere().equals(genere)){
                ret.add(l);
            }
        }
        return ret;*/



    //ORDINAMENTO
    /*public void ordinaPerTitolo(List<Libro> libri, boolean crescente){
        libri.sort((l1, l2) -> {
            if (crescente)
                return l1.getTitolo().compareToIgnoreCase(l2.getTitolo());
            else
                return l2.getTitolo().compareToIgnoreCase(l1.getTitolo());
        });
        daVisualizzare = libri;
        notifyObservers();
    }

    public void ordinaPerAutore(List<Libro> libri, boolean crescente){
        libri.sort((l1, l2) -> {
            if (crescente)
                return l1.getAutore().compareToIgnoreCase(l2.getAutore());
            else
                return l2.getAutore().compareToIgnoreCase(l1.getAutore());
        });
        daVisualizzare = libri;
        notifyObservers();
    }

    public void ordinaPerValutazione(List<Libro> libri, boolean crescente) {
        /*libri.sort((l1, l2) -> {
            if (crescente)
                return l1.getValutazione() > l2.getValutazione() ? 1 : -1;
            else
                return l1.getValutazione() < l2.getValutazione() ? 1 : -1;
        });
        daVisualizzare = libri;
        notifyObservers();
    }*/



    //ORDINA
    public void ordina(OrdinamentoStrategy strategia, List<Libro> libri, boolean crescente) {
        strategia.ordina(libri, crescente);
        daVisualizzare = libri;
        notifyObservers();
    }


    //UPDATE PER OBSERVER
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


 /*public List<Libro> filtraPerGenere(Genere genere, List<Libro> libriDaFiltrare){
        List<Libro> ret = new ArrayList<>();
        for(Libro l : libriDaFiltrare){
            if(l.getGenere().equals(genere)){
                ret.add(l);
            }
        }
        return ret;
    }
    public List<Libro> filtraPerStatoLettura(StatoLettura stato, List<Libro> libriDaFiltrare){
        List<Libro> ret = new ArrayList<>();
        for(Libro l : libriDaFiltrare){
            if(l.getStatoLettura().equals(stato)){
                ret.add(l);
            }
        }
        return ret;
    }*/
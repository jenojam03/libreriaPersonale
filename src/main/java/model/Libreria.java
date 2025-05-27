package model;
import UI.*;
import java.util.*;


//classe che rappresenta il sistema
public class Libreria {

    private Map<String, Libro> libri = new HashMap<>();
    private List<ObserverIF> observers = new ArrayList<>();

    public boolean aggiungiLibro(Libro libro) {
        //prevenzione duplicati
        if(libri.containsKey(libro.getISBN())){
            return false;
        }
        libri.put(libro.getISBN(), libro);
        notifyObservers();
        return true;
    }

    public void rimuoviLibro(String isbn) {
        libri.remove(isbn);
        notifyObservers();
    }

    public void modificaLibro(String isbn, Libro libroAggiornato) {
        libri.put(isbn, libroAggiornato);
        notifyObservers();
    }

    public Libro getLibro(String isbn) {
        return libri.get(isbn);
    }

    public Map<String,Libro> getTuttiILibri() {
        return libri;
    }

    public void setLibri(Map<String, Libro> libris) {
        libri.clear();
        libri.putAll(libris);
        notifyObservers();
    }

    //RICERCA E FILTRAGGIO
    public List<Libro> cercaPerTitolo(String titolo){
        List<Libro> ret = new ArrayList<>();
        if(titolo == null){
            return ret;
        }

        for(Libro l : libri.values()){
            if(l.getTitolo().toLowerCase().contains(titolo.trim().toLowerCase())){
                ret.add(l);
            }
        }
        return ret;

    }

    public List<Libro> cercaPerAutore(String autore){
        List<Libro> ret = new ArrayList<>();
        if(autore == null){
            return ret;
        }

        for(Libro l : libri.values()){
            if(l.getAutore().toLowerCase().contains(autore.trim().toLowerCase())){
                ret.add(l);
            }
        }
        return ret;
    }


    public Libro cercaPerISBN(String isbn){
        return libri.get(isbn);
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

    public List<Libro> filtra(Genere genere, StatoLettura stato, List<Libro> libriDaFiltrare) {
        List<Libro> ret = new ArrayList<>();

        for (Libro l : libriDaFiltrare) {
            boolean matchGenere = (genere == null || l.getGenere().equals(genere));
            boolean matchStato = (stato == null || l.getStatoLettura().equals(stato));

            if (matchGenere && matchStato) {
                ret.add(l);
            }
        }

        return ret;

    }
        /*List<Libro> ret = new ArrayList<>();
        for(Libro l : libriDaFiltrare){
            if(l.getStatoLettura().equals(stato) && l.getGenere().equals(genere)){
                ret.add(l);
            }
        }
        return ret;*/



    //ORDINAMENTO
    public List<Libro> ordinaPerTitolo(List<Libro> libri, boolean crescente){
        Collections.sort(libri, new Comparator<Libro>() {
            @Override
            public int compare(Libro l1, Libro l2) {
                if(crescente)
                    return l1.getTitolo().compareToIgnoreCase(l2.getTitolo());
                else
                    return l2.getTitolo().compareToIgnoreCase(l1.getTitolo());
            }
        });
        return libri;
    }

    public List<Libro> ordinaPerAutore(List<Libro> libri, boolean crescente){
        Collections.sort(libri, (l1, l2) -> {
            if (crescente)
                return l1.getAutore().compareToIgnoreCase(l2.getAutore());
            else
                return l2.getAutore().compareToIgnoreCase(l1.getAutore());
        });
        return libri;
    }

    public List<Libro> ordinaPerValutazione(List<Libro> libri, boolean crescente) {
        Collections.sort(libri, (l1, l2) -> {
            if (crescente)
                return l1.getValutazione() > l2.getValutazione() ? 1 : -1;
            else
                return l1.getValutazione() < l2.getValutazione() ? 1 : -1;
        });
        return libri;
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
            o.update(libri);
        }
    }
}

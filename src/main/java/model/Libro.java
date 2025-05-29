package model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Libro {

    private final String titolo;
    private final String autore;
    private final String ISBN;
    private final Genere Genere;
    private StatoLettura statoLettura;
    private int valutazione = 0; //DA 1 A 5
    private String note;


    //usato per la conversione JSON
    public Libro(@JsonProperty("titolo") String titolo,
                 @JsonProperty("autore") String autore,
                 @JsonProperty("isbn") String ISBN,
                 @JsonProperty("genere") Genere Genere,
                 @JsonProperty("statoLettura") StatoLettura statoLettura,
                 @JsonProperty("valutazione") int valutazione,
                 @JsonProperty("note") String note) {
            this.titolo = titolo;
            this.autore = autore;
            this.ISBN = ISBN;
            this.Genere = Genere;
            this.statoLettura = statoLettura;
            this.valutazione = valutazione;
            this.note = note;
    }

    //di default si chiede all'utente di inserire solo questi campi, i restanti possono essere modificati poi
    public Libro(String titolo, String autore, String isbn, Genere genere){
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = isbn;
        this.Genere = genere;
        //campi di default
        this.statoLettura = StatoLettura.DA_LEGGERE;
        this.valutazione = 0;
        this.note = "";
    }


    //GETTER E SETTER
    public String getAutore() {
        return autore;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getISBN() {
        return ISBN;
    }


    public Genere getGenere() {
        return Genere;
    }


    public StatoLettura getStatoLettura() {
        return statoLettura;
    }

    public int getValutazione() {
        return valutazione;
    }

    public String getNote() {
        return note;
    }

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }


    public void setValutazione(int valutazione) {
        if(valutazione <= 5 && valutazione >= 0)
            this.valutazione = valutazione;
    }

    public void setNote(String note) {
        this.note = note;
    }



    @Override
    public String toString() {
        return "Libro{" +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", isbn='" + ISBN + '\'' +
                ", genere=" + Genere + + '\''+
                ", statoLettura=" + statoLettura+'\''+
                ", valutazione=" + valutazione +'\''+
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Libro libro = (Libro) obj;
        return libro.getISBN().equals(this.getISBN());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.ISBN);
        return hash;
    }
}

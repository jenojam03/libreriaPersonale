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

    /*//INNER CLASS BUILDER PER LA COSTRUZIONE------------------------------------------------------------
    public static class LibroBuilder {

        //Parametri obbligatori
        private final String titolo;
        private final String autore;
        private final String ISBN;
        private final Genere Genere;

        //Parametri opzionali (inizializzazioni di default)
        private StatoLettura statoLettura = StatoLettura.DA_LEGGERE;
        private int valutazione = 0;
        private String note = "";

        public LibroBuilder(String titolo, String autore, String ISBN, Genere Genere) {
            this.titolo = titolo;
            this.autore=autore;
            this.ISBN = ISBN;
            this.Genere = Genere;
        }

        public LibroBuilder setStatoLettura(StatoLettura statoLettura) {
            this.statoLettura = statoLettura;
            return this;
        }

        public LibroBuilder setValutazione(int valutazione) {
            if(valutazione <= 5 && valutazione >= 1)
                this.valutazione = valutazione;
            return this;
        }

        public LibroBuilder setNote(String note) {
            this.note = note;
            return this;
        }

        public Libro build(){
            return new Libro(this);
        }
    }
*/
    //------------------------------------------------------------------------------

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

    public Libro(String titolo, String autore, String isbn, Genere genere){
        this.titolo = titolo;
        this.autore = autore;
        this.ISBN = isbn;
        this.Genere = genere;
        this.statoLettura = StatoLettura.DA_LEGGERE;
        this.valutazione = 0;
        this.note = "";
    }

    /*public Libro(LibroBuilder b){
        titolo = b.titolo;
        autore = b.autore;
        ISBN = b.ISBN;
        Genere = b.Genere;
        statoLettura = b.statoLettura;
        valutazione = b.valutazione;
        note = b.note;
    }*/

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

    public void setStatoLettura(StatoLettura statoLettura) {
        this.statoLettura = statoLettura;
    }

    public int getValutazione() {
        return valutazione;
    }

    public void setValutazione(int valutazione) {
        if(valutazione <= 5 && valutazione >= 0)
            this.valutazione = valutazione;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
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

package persistence;

import UI.COSTANTI;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Libro;


import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonStorageManager {
    private static File file = new File(COSTANTI.percorso+"/catalogo.json");
    private static final ObjectMapper mapper = new ObjectMapper();



    // Carica da JSON e restituisce una mappa ISBN -> model.Libro
    public static Map<String, Libro> caricamento() throws IOException {
        if (!file.exists()){
            file = new File(COSTANTI.percorso+"/catalogo.json");
            return new HashMap<>();
        }


        Map<String, Libro> libri = mapper.readValue(file, new TypeReference<Map<String, Libro>>() {});

        return libri;
    }

    // Salva una mappa ISBN -> model.Libro come lista ordinaria
    public static void salvataggio(Map<String, Libro> libri) throws IOException {
        mapper.writerWithDefaultPrettyPrinter().writeValue(file, libri);
    }

    /*
    PER IL TESTING

    public static void main(String[] args) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        /*Map<String, Libro> libri = new HashMap<>();
        Libro.LibroBuilder lb = new Libro.LibroBuilder("Piccole donne","Louisa May Alcott","12345677", Genere.ROMANZO);
        lb.setStatoLettura(StatoLettura.LETTO);
        lb.setValutazione(5);
        Libro l = lb.build();
        libri.put(l.getISBN(), l);
        Libro.LibroBuilder lb1 = new Libro.LibroBuilder("Il codice da vinci","Dan Brown","9876543", Genere.GIALLO);
        lb.setStatoLettura(StatoLettura.LETTO);
        lb.setValutazione(5);
        Libro l1 = lb1.build();
        libri.put(l1.getISBN(), l1);
        File file = new File("C:\\Users\\giada\\Desktop\\file.json");
        //mapper.writerWithDefaultPrettyPrinter().writeValue(file, libri);

        Map<String, Libro> libri = mapper.readValue(file, new TypeReference<Map<String, Libro>>() {});
        System.out.println(hashMapToString(libri));


    }

    public static <K, V> String hashMapToString(Map<K, V> map) {
        if (map == null) {
            return "La mappa è null.";
        }
        if (map.isEmpty()) {
            return "La mappa è vuota.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Contenuto della Mappa: {\n");

        // Itera su ogni entry della mappa
        for (Map.Entry<K, V> entry : map.entrySet()) {
            sb.append("  "); // Indentazione per leggibilità
            sb.append(entry.getKey() == null ? "null" : entry.getKey().toString());
            sb.append(": ");
            sb.append(entry.getValue() == null ? "null" : entry.getValue().toString());
            sb.append("\n");
        }

        sb.append("}");
        return sb.toString();
    }*/
}

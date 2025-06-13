package test;

import model.Libro;
import model.Genere;
import model.StatoLettura;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import strategy.OrdinaPerAutore;
import strategy.OrdinaPerTitolo;
import strategy.OrdinaPerValutazione;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class OrdinamentoStrategyTest {

    private List<Libro> libri;

    @BeforeEach
    void setup() {
        Libro l1 = new Libro("Zorro", "Mario", "001", Genere.ROMANZO);
        l1.setValutazione(3);
        l1.setStatoLettura(StatoLettura.LETTO);

        Libro l2 = new Libro("Aquila", "Anna", "002", Genere.FANTASY);
        l2.setValutazione(5);
        l2.setStatoLettura(StatoLettura.IN_LETTURA);

        Libro l3 = new Libro("Mondo", "Luca", "003", Genere.SAGGIO);
        l3.setValutazione(1);
        l3.setStatoLettura(StatoLettura.DA_LEGGERE);

        libri = new ArrayList<>(List.of(l1, l2, l3));
    }

    @Test
    void testOrdinaPerAutoreCrescente() {
        OrdinaPerAutore strategy = new OrdinaPerAutore();
        strategy.ordina(libri, true);
        assertEquals("Anna", libri.get(0).getAutore());
        assertEquals("Luca", libri.get(1).getAutore());
        assertEquals("Mario", libri.get(2).getAutore());
    }

    @Test
    void testOrdinaPerAutoreDecrescente() {
        OrdinaPerAutore strategy = new OrdinaPerAutore();
        strategy.ordina(libri, false);
        assertEquals("Mario", libri.get(0).getAutore());
        assertEquals("Luca", libri.get(1).getAutore());
        assertEquals("Anna", libri.get(2).getAutore());
    }

    @Test
    void testOrdinaPerTitoloCrescente() {
        OrdinaPerTitolo strategy = new OrdinaPerTitolo();
        strategy.ordina(libri, true);
        assertEquals("Aquila", libri.get(0).getTitolo());
        assertEquals("Mondo", libri.get(1).getTitolo());
        assertEquals("Zorro", libri.get(2).getTitolo());
    }

    @Test
    void testOrdinaPerTitoloDecrescente() {
        OrdinaPerTitolo strategy = new OrdinaPerTitolo();
        strategy.ordina(libri, false);
        assertEquals("Zorro", libri.get(0).getTitolo());
        assertEquals("Mondo", libri.get(1).getTitolo());
        assertEquals("Aquila", libri.get(2).getTitolo());
    }

    @Test
    void testOrdinaPerValutazioneCrescente() {
        OrdinaPerValutazione strategy = new OrdinaPerValutazione();
        strategy.ordina(libri, true);
        assertEquals(1, libri.get(0).getValutazione());
        assertEquals(3, libri.get(1).getValutazione());
        assertEquals(5, libri.get(2).getValutazione());
    }

    @Test
    void testOrdinaPerValutazioneDecrescente() {
        OrdinaPerValutazione strategy = new OrdinaPerValutazione();
        strategy.ordina(libri, false);
        assertEquals(5, libri.get(0).getValutazione());
        assertEquals(3, libri.get(1).getValutazione());
        assertEquals(1, libri.get(2).getValutazione());
    }

    @Test
    void testOrdinaListaVuota() {
        List<Libro> vuota = new ArrayList<>();
        new OrdinaPerAutore().ordina(vuota, true);
        new OrdinaPerTitolo().ordina(vuota, false);
        new OrdinaPerValutazione().ordina(vuota, true);
        assertTrue(vuota.isEmpty());
    }
}

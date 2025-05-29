package UI;

import controller.FacadeLibreria;
import model.*;
import strategy.OrdinaPerAutore;
import strategy.OrdinaPerTitolo;
import strategy.OrdinaPerValutazione;
import strategy.OrdinamentoStrategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class GUI extends JFrame implements ObserverIF {

    private FacadeLibreria facade;
    private JPanel cardsPanel;
    private List<Libro> libriVisualizzati = new ArrayList<>();
    private List<Libro> risultatiBase;
    private JButton undoButton;
    private boolean ricercaAttiva = false;


    //per il filtraggio
    private Genere filtroGenere;
    private StatoLettura filtroStato;

    public GUI() {
        try {
            COSTANTI.percorso = ConfigManager.caricaPercorso();

            if (COSTANTI.percorso == null) {
                JDialog dialog = new JDialog((Frame) null, "Inserisci percorso", true);
                JTextField campoPercorso = new JTextField(30);
                JButton salvaButton = new JButton("Salva percorso");

                JPanel panel = new JPanel(new BorderLayout(5, 5));
                panel.add(new JLabel("Inserisci percorso:"), BorderLayout.NORTH);
                panel.add(campoPercorso, BorderLayout.CENTER);
                panel.add(salvaButton, BorderLayout.SOUTH);

                dialog.getContentPane().add(panel);
                dialog.pack();
                dialog.setLocationRelativeTo(null);

                salvaButton.addActionListener(e -> {
                    String testo = campoPercorso.getText().trim();
                    if (testo.isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Inserisci un percorso valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                    } else {
                        COSTANTI.percorso = testo;
                        ConfigManager.salvaPercorso(testo);
                        dialog.dispose();
                    }
                });

                dialog.setVisible(true);

                if (COSTANTI.percorso == null) {
                    JOptionPane.showMessageDialog(null, "Nessun percorso selezionato. L'app verrà chiusa.");
                    System.exit(0);
                }
            }

            this.facade = new FacadeLibreria();
            facade.getLibreria().attach(this);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        libriVisualizzati = facade.getDaVisualizzare();
        updateCards(libriVisualizzati);
    }

    private void costruisciInterfaccia() {
        JFrame frame = new JFrame("Libreria");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());


        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton redoButton = new JButton("Redo");
        //bottone undo
        undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            try {
                facade.undo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            redoButton.setEnabled(true);
        });


        //bottone redo

        redoButton.addActionListener(e -> {
            try {
                facade.redo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            undoButton.setEnabled(true);
        });

        //bottone aggiungi libro
        JButton aggiungiBtn = new JButton("Aggiungi libro");
        aggiungiBtn.setEnabled(!ricercaAttiva);
        aggiungiBtn.addActionListener(ev -> {mostraFinestraAggiunta(); undoButton.setEnabled(true);} );


        //barra di ricerca
        JTextField searchField = new JTextField(20);
        searchField.addActionListener(e -> {

            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                facade.ricerca(query);
                aggiungiBtn.setEnabled(false);
                //memorizzo i risultati della ricerca per poterli filtrare in modi diversi
                risultatiBase = facade.getDaVisualizzare();
            }
        });

        //indietro dalla ricerca
        JButton indietroButton = new JButton("Indietro");
        indietroButton.addActionListener(e -> {
            facade.mostraTutti(); // metodo esistente nella tua classe Libreria
            filtroStato = null;
            filtroGenere = null;
            aggiungiBtn.setEnabled(true);
            risultatiBase=facade.getDaVisualizzare();
        });



        //bottone per il filtro
        JButton filtroBtn = new JButton("Filtra");
        filtroBtn.addActionListener(e -> mostraFinestraFiltri());

        String[] ordini = {
                "Titolo (A-Z)", "Titolo (Z-A)",
                "Autore (A-Z)", "Autore (Z-A)",
                "Valutazione (crescente)", "Valutazione (decrescente)"
        };
        JComboBox<String> ordinaBox = new JComboBox<>(ordini);
        ordinaBox.addActionListener(eve -> {

            OrdinamentoStrategy strategy;
            String criterio = (String) ordinaBox.getSelectedItem();
            switch (criterio) {
                case "Titolo (A-Z)" -> facade.ordina(new OrdinaPerTitolo(),true);
                case "Titolo (Z-A)" -> facade.ordina(new OrdinaPerTitolo(),false);
                case "Autore (A-Z)" -> facade.ordina(new OrdinaPerAutore(), true);
                case "Autore (Z-A)" -> facade.ordina(new OrdinaPerAutore(), false);
                case "Valutazione (crescente)" -> facade.ordina(new OrdinaPerValutazione(), true);
                case "Valutazione (decrescente)" -> facade.ordina(new OrdinaPerValutazione(), false);
            };
            //update();
        });

        topPanel.add(indietroButton);
        topPanel.add(new JLabel("Cerca:"));
        topPanel.add(searchField);
        topPanel.add(filtroBtn);
        topPanel.add(new JLabel("Ordina per:"));
        topPanel.add(ordinaBox);

        cardsPanel = new JPanel(new WrapLayout(FlowLayout.LEFT, 15, 15));
        JScrollPane scrollPane = new JScrollPane(cardsPanel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));


        redoButton.setEnabled(false);
        undoButton.setEnabled(false);
        leftPanel.add(undoButton);
        leftPanel.add(redoButton);
        rightPanel.add(aggiungiBtn);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        facade.mostraTutti();

        //inizializzato all'inizio
        risultatiBase = facade.getDaVisualizzare();

        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

    private void updateCards(List<Libro> libri) {
        cardsPanel.removeAll();
        for (Libro libro : libri) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            card.setPreferredSize(new Dimension(180, 120));
            card.add(new JLabel("Titolo: " + libro.getTitolo()));
            card.add(new JLabel("Autore: " + libro.getAutore()));
            card.add(new JLabel("ISBN: " + libro.getISBN()));
            card.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    mostraFinestraModifica(libro);
                    undoButton.setEnabled(true);
                }
            });
            cardsPanel.add(card);
        }
        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void mostraFinestraAggiunta() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Nuovo libro");
        dialog.setModal(true);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField titolo = new JTextField();
        JTextField autore = new JTextField();
        JTextField isbn = new JTextField();
        JComboBox<Genere> genereBox = new JComboBox<>(Genere.values());

        JButton salva = new JButton("Salva");
        salva.addActionListener(e -> {
            Libro libro = new Libro(
                    titolo.getText(),
                    autore.getText(),
                    isbn.getText(),
                    (Genere) genereBox.getSelectedItem()
            );


            try {
                boolean ret = facade.aggiungiLibro(libro);
                if (!ret) {
                    JOptionPane.showMessageDialog(dialog, "Il libro inserito esiste già. Correggi l'ISBN.", "Libro duplicato", JOptionPane.WARNING_MESSAGE);
                    return; // NON chiudere la finestra, consenti la correzione
                }
                //dialog.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Errore durante l'aggiunta del libro.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });

        dialog.add(new JLabel("Titolo:")); dialog.add(titolo);
        dialog.add(new JLabel("Autore:")); dialog.add(autore);
        dialog.add(new JLabel("ISBN:")); dialog.add(isbn);
        dialog.add(new JLabel("Genere:")); dialog.add(genereBox);
        dialog.add(new JLabel()); dialog.add(salva);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    private void mostraFinestraModifica(Libro libro) {

        JDialog dialog = new JDialog();
        dialog.setTitle("Modifica Libro");
        dialog.setModal(true);
        dialog.setLayout(new GridLayout(0, 2, 5, 5));

        JTextField titolo = new JTextField(libro.getTitolo());
        titolo.setEditable(false);

        JTextField autore = new JTextField(libro.getAutore());
        autore.setEditable(false);

        JTextField isbn = new JTextField(libro.getISBN());
        isbn.setEditable(false);

        JComboBox<StatoLettura> statoBox = new JComboBox<>(StatoLettura.values());
        statoBox.setSelectedItem(libro.getStatoLettura());

        JSpinner valutazioneSpinner = new JSpinner(new SpinnerNumberModel(libro.getValutazione(), 0, 5, 1));

        JButton salva = new JButton("Modifica");
        salva.addActionListener(e -> {

            Libro aggiornato = new Libro(
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getISBN(),
                    libro.getGenere(),
                    (StatoLettura) statoBox.getSelectedItem(),
                    (Integer) valutazioneSpinner.getValue(),
                    libro.getNote() // o puoi anche permettere di modificare le note, se vuoi
            );

            try {
                facade.modificaLibro(libro.getISBN(), aggiornato);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Errore durante la modifica.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
            undoButton.setEnabled(true);
        });


        //bottone rimuovi
        JButton rimuovi = new JButton("Rimuovi libro");
        rimuovi.addActionListener(e -> {

            int scelta = JOptionPane.showConfirmDialog(dialog, "Sei sicuro di voler rimuovere il libro?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (scelta == JOptionPane.YES_OPTION) {
                try {
                    facade.rimuoviLibro(libro.getISBN());
                    undoButton.setEnabled(true);

                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(dialog, "Errore durante la rimozione.", "Errore", JOptionPane.ERROR_MESSAGE);
                }
                //update();
                dialog.dispose();
            }
        });

        dialog.add(new JLabel("Titolo:")); dialog.add(titolo);
        dialog.add(new JLabel("Autore:")); dialog.add(autore);
        dialog.add(new JLabel("ISBN:")); dialog.add(isbn);
        dialog.add(new JLabel("Stato lettura:")); dialog.add(statoBox);
        dialog.add(new JLabel("Valutazione:")); dialog.add(valutazioneSpinner);
        dialog.add(salva); dialog.add(rimuovi);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void mostraFinestraFiltri() {
        JDialog dialog = new JDialog(this, "Filtra libri", true);
        dialog.setSize(400, 300); // Aumentata
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel filtriPanel = new JPanel(new GridLayout(1, 2));

        // ===== GENERE =====
        JPanel generePanel = new JPanel();
        generePanel.setLayout(new BoxLayout(generePanel, BoxLayout.Y_AXIS));
        generePanel.setBorder(BorderFactory.createTitledBorder("Genere"));

        List<JToggleButton> genereButtons = new ArrayList<>();
        final JToggleButton[] selezionatoGenere = {null};

        for (Genere g : Genere.values()) {
            JToggleButton btn = new JToggleButton(g.toString());
            btn.setMaximumSize(new Dimension(200, 25)); // Rimpiccioliti
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (filtroGenere != null && g == filtroGenere) {
                btn.setSelected(true);
                selezionatoGenere[0] = btn;
            }

            btn.addActionListener(e -> {
                if (btn.equals(selezionatoGenere[0])) {
                    btn.setSelected(false);
                    selezionatoGenere[0] = null;
                } else {
                    for (JToggleButton b : genereButtons) {
                        b.setSelected(false);
                    }
                    btn.setSelected(true);
                    selezionatoGenere[0] = btn;
                }
            });

            genereButtons.add(btn);
            generePanel.add(btn);
        }

        JScrollPane genereScroll = new JScrollPane(generePanel);
        genereScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // ===== STATO LETTURA =====
        JPanel statoPanel = new JPanel();
        statoPanel.setLayout(new BoxLayout(statoPanel, BoxLayout.Y_AXIS));
        statoPanel.setBorder(BorderFactory.createTitledBorder("Stato lettura"));

        List<JToggleButton> statoButtons = new ArrayList<>();
        final JToggleButton[] selezionatoStato = {null};

        for (StatoLettura s : StatoLettura.values()) {
            JToggleButton btn = new JToggleButton(s.toString());
            btn.setMaximumSize(new Dimension(200, 25));
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);

            if (filtroStato != null && s == filtroStato) {
                btn.setSelected(true);
                selezionatoStato[0] = btn;
            }

            btn.addActionListener(e -> {
                if (btn.equals(selezionatoStato[0])) {
                    btn.setSelected(false);
                    selezionatoStato[0] = null;
                } else {
                    for (JToggleButton b : statoButtons) {
                        b.setSelected(false);
                    }
                    btn.setSelected(true);
                    selezionatoStato[0] = btn;
                }
            });

            statoButtons.add(btn);
            statoPanel.add(btn);
        }

        filtriPanel.add(genereScroll);
        filtriPanel.add(statoPanel);

        // ===== BOTTONE FILTRA =====
        JButton filtraBtn = new JButton("Filtra");
        filtraBtn.addActionListener(e -> {
            filtroGenere = null;
            filtroStato = null;

            if (selezionatoGenere[0] != null) {
                filtroGenere = Genere.valueOf(selezionatoGenere[0].getText());
            }

            if (selezionatoStato[0] != null) {
                filtroStato = StatoLettura.valueOf(selezionatoStato[0].getText());
            }

            facade.filtra(filtroGenere, filtroStato, risultatiBase);
            dialog.dispose();
        });

        JPanel btnPanel = new JPanel();
        btnPanel.add(filtraBtn);

        dialog.add(filtriPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.costruisciInterfaccia();
        });
    }

}

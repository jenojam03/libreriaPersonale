package UI;

import controller.FacadeLibreria;
import model.*;
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

        //barra di ricerca
        JTextField searchField = new JTextField(20);
        searchField.addActionListener(e -> {

            String query = searchField.getText().trim();
            if (!query.isEmpty()) {
                facade.ricerca(query);

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

            String criterio = (String) ordinaBox.getSelectedItem();
            switch (criterio) {
                case "Titolo (A-Z)" -> facade.ordina(CriterioOrdinamento.TITOLO, true);
                case "Titolo (Z-A)" -> facade.ordina(CriterioOrdinamento.TITOLO, false);
                case "Autore (A-Z)" -> facade.ordina(CriterioOrdinamento.AUTORE, true);
                case "Autore (Z-A)" -> facade.ordina(CriterioOrdinamento.AUTORE, false);
                case "Valutazione (crescente)" -> facade.ordina(CriterioOrdinamento.VALUTAZIONE, true);
                case "Valutazione (decrescente)" -> facade.ordina(CriterioOrdinamento.VALUTAZIONE, false);
                default -> new ArrayList<>();
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

        //bottone undo
        JButton undoButton = new JButton("Undo");
        undoButton.addActionListener(e -> {
            try {
                facade.undo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        //bottone redo
        JButton redoButton = new JButton("Redo");
        redoButton.addActionListener(e -> {
            try {
                facade.redo();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });


        //bottone aggiungi libro
        JButton aggiungiBtn = new JButton("Aggiungi libro");
        aggiungiBtn.addActionListener(ev -> mostraFinestraAggiunta());

        leftPanel.add(undoButton);
        leftPanel.add(redoButton);
        rightPanel.add(aggiungiBtn);

        bottomPanel.add(leftPanel, BorderLayout.WEST);
        bottomPanel.add(rightPanel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        facade.mostraTutti();
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
            Libro libro = new Libro.LibroBuilder(
                    titolo.getText(),
                    autore.getText(),
                    isbn.getText(),
                    (Genere) genereBox.getSelectedItem()
            ).build();

            try {
                boolean ret = facade.aggiungiLibro(libro);
                if (!ret) {
                    JOptionPane.showMessageDialog(dialog, "Il libro inserito esiste già. Correggi l'ISBN.", "Libro duplicato", JOptionPane.WARNING_MESSAGE);
                    return; // NON chiudere la finestra, consenti la correzione
                }
                dialog.dispose();
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
            Libro aggiornato = new Libro.LibroBuilder(
                    libro.getTitolo(),
                    libro.getAutore(),
                    libro.getISBN(),
                    libro.getGenere()
            ).setStatoLettura((StatoLettura) statoBox.getSelectedItem())
                    .setValutazione((Integer) valutazioneSpinner.getValue())
                    .build();

            try {
                facade.modificaLibro(libro.getISBN(), aggiornato);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Errore durante la modifica.", "Errore", JOptionPane.ERROR_MESSAGE);
            }
            dialog.dispose();
        });


        //bottone rimuovi
        JButton rimuovi = new JButton("Rimuovi libro");
        rimuovi.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(dialog, "Sei sicuro di voler rimuovere il libro?", "Conferma", JOptionPane.YES_NO_OPTION);
            if (scelta == JOptionPane.YES_OPTION) {
                try {
                    facade.rimuoviLibro(libro.getISBN());

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
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Pannello principale per i filtri
        JPanel filtriPanel = new JPanel(new GridLayout(1, 2));

        // Pannello Genere
        JPanel generePanel = new JPanel();
        generePanel.setLayout(new BoxLayout(generePanel, BoxLayout.Y_AXIS));
        generePanel.setBorder(BorderFactory.createTitledBorder("Genere"));

        ButtonGroup gruppoGeneri = new ButtonGroup();
        JRadioButton[] generiBox = new JRadioButton[Genere.values().length];
        for (int i = 0; i < Genere.values().length; i++) {
            generiBox[i] = new JRadioButton(Genere.values()[i].toString());
            gruppoGeneri.add(generiBox[i]);
            generePanel.add(generiBox[i]);

            // Se c'è un filtro attivo, seleziona il radio button corrispondente
            if (filtroGenere != null && Genere.values()[i] == filtroGenere) {
                generiBox[i].setSelected(true);
            }
        }


        // Pannello Stato
        JPanel statoPanel = new JPanel();
        statoPanel.setLayout(new BoxLayout(statoPanel, BoxLayout.Y_AXIS));
        statoPanel.setBorder(BorderFactory.createTitledBorder("Stato lettura"));

        ButtonGroup gruppoStati = new ButtonGroup();
        JRadioButton[] statiBox = new JRadioButton[StatoLettura.values().length];
        for (int i = 0; i < StatoLettura.values().length; i++) {
            statiBox[i] = new JRadioButton(StatoLettura.values()[i].toString());
            gruppoStati.add(statiBox[i]);
            statoPanel.add(statiBox[i]);

            // Se c'è un filtro attivo, seleziona il radio button corrispondente
            if (filtroStato != null && StatoLettura.values()[i] == filtroStato) {
                statiBox[i].setSelected(true);
            }
        }

        filtriPanel.add(generePanel);
        filtriPanel.add(statoPanel);

        // Bottone filtra
        JButton filtraBtn = new JButton("Filtra");
        filtraBtn.addActionListener(e -> {

            Genere genere = null;
            StatoLettura stato = null;


            for (int i = 0; i < generiBox.length; i++) {
                if (generiBox[i].isSelected()) {
                    genere = Genere.values()[i];
                    filtroGenere = genere;
                    break;
                }
            }

            for (int i = 0; i < statiBox.length; i++) {
                if (statiBox[i].isSelected()) {
                    stato = StatoLettura.values()[i];
                    filtroStato = stato;
                    break;
                }
            }


            facade.filtra(genere, stato, risultatiBase);

            dialog.dispose();
        });

        /*JButton rimuoviFiltriBtn = new JButton("Rimuovi filtri");
        rimuoviFiltriBtn.addActionListener(e -> {
            //update();
            filtroGenere = null;
            filtroStato = null;
            dialog.dispose();
        });*/

        JPanel btnPanel = new JPanel();
        btnPanel.add(filtraBtn);
        //btnPanel.add(rimuoviFiltriBtn);

        dialog.add(filtriPanel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);;


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.costruisciInterfaccia();
        });
    }

}

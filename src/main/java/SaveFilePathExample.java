import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

    public class SaveFilePathExample extends JFrame {

        private JTextField pathField;  // campo per mostrare il percorso selezionato
        private JButton browseButton;  // bottone per aprire la finestra di dialogo

        public SaveFilePathExample() {
            super("Seleziona percorso salvataggio");

            // Imposta layout
            setLayout(new BorderLayout(5,5));

            // Campo di testo per mostrare il percorso scelto
            pathField = new JTextField();
            pathField.setEditable(false);

            // Bottone per aprire JFileChooser
            browseButton = new JButton("Scegli percorso...");
            browseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    scegliPercorso();
                }
            });

            // Pannello superiore con campo e bottone
            JPanel panel = new JPanel(new BorderLayout(5,5));
            panel.add(pathField, BorderLayout.CENTER);
            panel.add(browseButton, BorderLayout.EAST);

            add(panel, BorderLayout.NORTH);

            setSize(500, 100);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null); // centra la finestra
        }

        private void scegliPercorso() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Scegli dove salvare il file");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY); // seleziona file singolo
            fileChooser.setApproveButtonText("Salva");

            int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String percorsoSalvataggio = fileChooser.getSelectedFile().getAbsolutePath();
                pathField.setText(percorsoSalvataggio);  // mostra il percorso nel campo
                // qui puoi salvare percorsoSalvataggio in una variabile o usarlo come vuoi
                System.out.println("Percorso scelto: " + percorsoSalvataggio);
            }
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                new SaveFilePathExample().setVisible(true);
            });
        }
    }



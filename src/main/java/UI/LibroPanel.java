package UI;

import model.Libro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class LibroPanel extends JPanel {

    public LibroPanel(Libro libro, Runnable onClick) {
        setPreferredSize(new Dimension(200, 100));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JLabel titolo = new JLabel("<html><b>" + libro.getTitolo() + "</b></html>");
        JLabel autore = new JLabel(libro.getAutore());

        add(titolo, BorderLayout.NORTH);
        add(autore, BorderLayout.CENTER);

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        });
    }
}


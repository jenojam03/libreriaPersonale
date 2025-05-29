package org.example;

import UI.GUI;
import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI gui = new GUI();
            gui.costruisciInterfaccia();
        });
    }
}
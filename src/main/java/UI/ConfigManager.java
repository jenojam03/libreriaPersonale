package UI;

import java.io.*;
import java.util.Properties;

    public class ConfigManager {
        private static final String CONFIG_FILE = "config.properties";

        public static String caricaPercorso() {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
                props.load(in);
                return props.getProperty("percorso");
            } catch (IOException e) {
                return null;
            }
        }

        public static void salvaPercorso(String percorso) {
            Properties props = new Properties();
            props.setProperty("percorso", percorso);
            try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
                props.store(out, "Configurazione Libreria");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



package juegopong_steveen_mark_raul_adrian_josep;

import BBDD.Conexio;
import LogicaJoc.Logica;
import Sonido.Sound;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Classe principal del joc Pong. Controla la inicialització, configuració de
 * l'idioma, entrada de nom de jugador, selecció de nivell i execució del bucle
 * principal del joc.
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class JuegoPong_Steveen_Mark_Raul_Adrian_Joep {

    /**
     * Amplada de la finestra del joc.
     */
    public static final int AMPLE_FINESTRA = 350;

    /**
     * Alçada de la finestra del joc.
     */
    public static final int ALT_FINESTRA = 400;

    /**
     * Mètode principal que inicia l'execució del joc Pong.
     *
     * @param args Arguments de la línia de comandaments (no utilitzats).
     * @throws InterruptedException Si el fil principal és interromput durant
     * l'espera.
     */
    public static void main(String[] args) throws InterruptedException {

        //Declarem e instanciem la varibale musica de tipus Sound
        Sound musica = new Sound();
        // Reprodueix la música del menú
        musica.reproducirMusicaMenu();

        // Inicialitza la connexió a la base de dades per traduccions
        Conexio conexio = new Conexio(null);

        // Opcions d'idioma disponibles
        String[] opcions = {"Català", "Castellano", "English"};

        // Mostra una finestra per seleccionar idioma
        int idiomaSeleccionat = JOptionPane.showOptionDialog(
                null,
                "Selecciona l'idioma:",
                "Idioma",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcions,
                opcions[0]
        );

        // Si no s'ha seleccionat cap idioma, es tanca el programa
        if (idiomaSeleccionat == -1) {
            JOptionPane.showMessageDialog(null, "No s'ha seleccionat cap idioma.");
            System.exit(0);
        }

        // Assigna la cadena corresponent a l'idioma escollit
        String idioma;
        switch (idiomaSeleccionat) {
            case 0:
                idioma = "Catala";
                break;
            case 1:
                idioma = "Castellano";
                break;
            case 2:
                idioma = "Angles";
                break;
            default:
                idioma = "Catala";
        }

        // Sol·licita el nom del jugador fins que introdueixi un nom vàlid
        String name = null;
        do {
            name = JOptionPane.showInputDialog(conexio.obtenirTraduccio("INSERT_NAME_TEXT", idioma));
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WORNG_NAME_TEXT", idioma));
                System.exit(0);
            }
        } while (name.trim().isEmpty());

        // Mostra missatge de benvinguda
        JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WELCOME_TEXT", idioma) + " " + name);

        // Mostra les normes del joc
        JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("RULES_TEXT", idioma));

        // Configura la finestra del joc
        JFrame frame = new JFrame("Mini Tennis");
        Logica l1 = new Logica(name, idioma);
        frame.add(l1);
        frame.setSize(AMPLE_FINESTRA, ALT_FINESTRA);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        l1.requestFocusInWindow();

        // Pregunta pel nivell inicial
        try {
            String nivellInicial = JOptionPane.showInputDialog(conexio.obtenirTraduccio("SELECT_LEVEL_TEXT", idioma));
            if (nivellInicial != null) {
                int nivellUsuari = Integer.parseInt(nivellInicial);
                l1.setNivell(nivellUsuari);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, conexio.obtenirTraduccio("WRONG_SELECT_LEVEL_TEXT", idioma));
        }

        // Comença la música de fons del joc
        musica.reproducirMusicaFondo();

        // Estableix la velocitat en funció del nivell escollit
        l1.setVelocitat(l1.getVELOCITAT_INICIAL() * (1 + l1.getIncrementVelocitat() * l1.getNivell()));

        // Bucle principal del joc: actualitza moviment i redibuixa cada 10ms
        while (true) {
            l1.moviment();
            l1.repaint();
            Thread.sleep(10);
        }
    }
}

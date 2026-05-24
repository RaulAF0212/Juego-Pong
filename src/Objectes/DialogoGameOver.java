package Objectes;

import javax.swing.*;
import java.awt.*;
import BBDD.Conexio;

/**
 * Diàleg que es mostra al final de la partida amb les puntuacions obtingudes.
 * Mostra una taula amb els jugadors, punts i dates, i permet tancar
 * l'aplicació.
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class DialogoGameOver extends JDialog {

    /**
     * Constructor del diàleg de Game Over. Crea un diàleg modal amb una taula
     * que mostra les puntuacions dels jugadors i un botó per sortir del joc.
     *
     * @param parent Finestra pare del diàleg.
     * @param datosPuntuaciones Matriu amb les dades de puntuació (jugador,
     * punts, data).
     * @param idioma Idioma seleccionat per a les traduccions de text.
     */
    public DialogoGameOver(JFrame parent, String[][] datosPuntuaciones, String idioma) {
        super(parent, true);
        setLayout(new BorderLayout(10, 10));

        Conexio conexio = new Conexio(null);

        setTitle(conexio.obtenirTraduccio("FINAL_GAME_TEXT", idioma));

        // Traducció dels encapçalaments de la taula
        String[] columnNames = {
            conexio.obtenirTraduccio("PLAYER_TEXT", idioma),
            conexio.obtenirTraduccio("POINT_TEXT", idioma),
            conexio.obtenirTraduccio("DATE_TEXT", idioma)
        };

        JTable tabla = new JTable(datosPuntuaciones, columnNames);
        tabla.setEnabled(false);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabla.setRowHeight(25);
        tabla.setGridColor(Color.LIGHT_GRAY);

        // Panell de botons
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton btnSalir = new JButton(conexio.obtenirTraduccio("EXIT_BUTTON", idioma));

        // Tanca l'aplicació quan es prem el botó de sortir
        btnSalir.addActionListener(e -> System.exit(0));

        panelBotones.add(btnSalir);
        add(panelBotones, BorderLayout.SOUTH);

        // Configuració del diàleg
        setSize(600, 400);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
}

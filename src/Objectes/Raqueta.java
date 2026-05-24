package Objectes;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import LogicaJoc.Logica;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

/**
 * Classe que representa la raqueta controlada pel jugador. Gestiona el moviment
 * lateral i la seva representació gràfica, així com la detecció de col·lisions
 * amb la pilota.
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class Raqueta extends JPanel {

    private static final int Y = 330;
    private static final int AMPLE_RAQUETA = 60;
    private static final int ALT_RAQUETA = 10;
    private int x = 130;
    private int xa = 0;
    private Logica logica;
    private Image raqueta;

    /**
     * Constructor de la classe Raqueta. Carrega la imatge de la raqueta i
     * guarda la referència a la lògica del joc.
     *
     * @param logica Instància de la lògica principal del joc.
     */
    public Raqueta(Logica logica) {
        this.logica = logica;
        // Ubicació de la imatge per la raqueta
        raqueta = new ImageIcon(getClass().getResource("/resources/imagenes/raquetaTenis.png")).getImage();
    }

    /**
     * Mètode que actualitza la posició de la raqueta segons el moviment actual.
     * Impedeix que surti dels límits del panell.
     */
    public void movimentRaqueta() {
        if (x + xa > 0 && x + xa < logica.getWidth() - AMPLE_RAQUETA) {
            x = x + xa;
        }
    }

    /**
     * Dibuixa la raqueta a la seva posició actual.
     *
     * @param g2d Objecte gràfic utilitzat per renderitzar.
     */
    public void paint(Graphics2D g2d) {
        g2d.drawImage(raqueta, x, Y, AMPLE_RAQUETA, ALT_RAQUETA, null);
    }

    /**
     * Deté el moviment de la raqueta quan es deixa anar la tecla.
     *
     * @param e Esdeveniment de tecla alliberada.
     */
    public void keyReleased(KeyEvent e) {
        xa = 0;
    }

    /**
     * Gestiona el moviment de la raqueta segons les tecles premudes. Admet tant
     * fletxes com les tecles A/D per moure a esquerra/dreta.
     *
     * @param e Esdeveniment de tecla premuda.
     */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
            xa = -(int) logica.getVelocitat();
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
            xa = (int) logica.getVelocitat();
        }
    }

    /**
     * Retorna els límits de la raqueta com un Rectangle, per detectar
     * col·lisions.
     *
     * @return Rectangle que representa la raqueta.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, Y, AMPLE_RAQUETA, ALT_RAQUETA);
    }
}

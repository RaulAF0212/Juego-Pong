package Objectes;

import java.awt.Graphics2D;
import javax.swing.JPanel;
import LogicaJoc.Logica;
import Sonido.Sound;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;
import java.util.Random;

/**
 * Classe que representa la pilota del joc. Gestiona el moviment, col·lisions i
 * la seva representació gràfica.
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class Pilota extends JPanel {

    private Random rand = new Random();
    private static final int DIAMETRE_PILOTA = 20;
    private int x = rand.nextInt(30, 320);
    private int y = 0;
    private int xa = 1;
    private int ya = 1;
    private Logica logica;
    private Image imatgePilota;
    private Sound musica= new Sound();

    /**
     * Constructor de la classe Pilota. Inicialitza la posició aleatòria i
     * carrega la imatge de la pilota.
     *
     * @param logica Referència a la lògica principal del joc.
     */
    public Pilota(Logica logica) {
        this.logica = logica;
        // Ubicació de la imatge de la pilota
        imatgePilota = new ImageIcon(getClass().getResource("/resources/imagenes/pelotaTenis.png")).getImage();
    }

    /**
     * Mètode que conté la lògica de moviment de la pilota. Controla les
     * col·lisions amb les parets, la raqueta i el límit inferior (game over).
     */
    public void movimentPilota() {
        boolean cambiarDireccion = true;

        if (x + xa * logica.getIncrementVelocitat() < 0) {
            xa = 1;
        } else if (x + xa * logica.getVelocitat() > logica.getWidth() - DIAMETRE_PILOTA) {
            xa = -1;
        } else if (y + ya * logica.getVelocitat() < 0) {
            ya = 1;
        } // Col·lisió només quan la pilota està descendint per evitar rebot doble
        else if (collision() && ya > 0) {
            Rectangle raqueta = logica.r1.getBounds();
            int raquetaCenter = raqueta.x + raqueta.width / 2;
            int pilotaCenter = x + DIAMETRE_PILOTA / 2;
            ya = -1; // Rebot vertical cap amunt

            // Calcula l'offset entre el centre de la raqueta i el de la pilota
            int offset = pilotaCenter - raquetaCenter;
            // Ajusta l'offset per evitar desviacions excessives
            xa = offset / (raqueta.width / 10);

            // Evita rebots totalment verticals
            if (xa == 0) {
                xa = (rand.nextBoolean()) ? 1 : -1;
            }

            // Col·loca la pilota just a sobre de la raqueta
            y = raqueta.y - DIAMETRE_PILOTA;
        } else {
            cambiarDireccion = false;
        }

        if (cambiarDireccion) {
            musica.reproducirRebotarPilota();
        }

        // Game over si toca la part inferior de la pantalla
        if (y + ya * logica.getVelocitat() > logica.getHeight() - DIAMETRE_PILOTA) {
            logica.gameOver();
        }

        // Actualitza la posició de la pilota
        x += xa * logica.getVelocitat();
        y += ya * logica.getVelocitat();
    }

    /**
     * Comprova si hi ha col·lisió amb la raqueta del jugador.
     *
     * @return true si hi ha col·lisió, false altrament.
     */
    private boolean collision() {
        return logica.r1.getBounds().intersects(getBounds());
    }

    /**
     * Dibuixa la pilota en la seva posició actual.
     *
     * @param g2d Objecte gràfic per pintar.
     */
    public void paint(Graphics2D g2d) {
        g2d.fillOval(x, y, 20, 20);
        g2d.drawImage(imatgePilota, x, y, DIAMETRE_PILOTA, DIAMETRE_PILOTA, null);
    }

    /**
     * Retorna el rectangle que envolta la pilota per gestionar col·lisions.
     *
     * @return Rectangle amb la posició i dimensions de la pilota.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, DIAMETRE_PILOTA, DIAMETRE_PILOTA);
    }
}

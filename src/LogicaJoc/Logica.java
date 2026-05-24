package LogicaJoc;

import javax.swing.*;

import BBDD.Conexio;
import Objectes.Pilota;
import Objectes.Raqueta;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import Objectes.DialogoGameOver;
import Sonido.Sound;

/**
 * Clase Logica que consta del funcionament base del videojoc
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class Logica extends JPanel {

    public Pilota p1 = new Pilota(this);
    public Raqueta r1 = new Raqueta(this);
    private final int VELOCITAT_INICIAL = 1;
    private long ultimTemps = System.currentTimeMillis();
    private int nivell = 1;
    private double velocitat = VELOCITAT_INICIAL;
    private int puntuacio = 0;
    private long ultimTempsPuntuacio = System.currentTimeMillis();
    private String playerName;
    private String idioma;
    private double incrementVelocitat = 0.5;
    private Image pista;
    private Image pista2;
    private Image pista3;
    private Conexio conexio;
    private Sound musica= new Sound();

    /**
     * Constructor de la classe Logica. Inicialitza el nom del jugador,
     * l'idioma, carrega les imatges de fons i configura el KeyListener per
     * capturar les tecles pressionades.
     *
     * @param nomIntroduit Nom del jugador introduït.
     * @param idiomaIntroduit Idioma seleccionat pel jugador.
     */
    public Logica(String nomIntroduit, String idiomaIntroduit) {
        this.playerName = nomIntroduit;
        this.idioma = idiomaIntroduit;
        conexio = new Conexio(this);
        //seleccionem la ruta de la imatge de fons
        pista = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis.png")).getImage();
        pista2 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis2.png")).getImage();
        pista3 = new ImageIcon(getClass().getResource("/resources/imagenes/pistaTenis3.png")).getImage();

        //Captació tecla pressionada 
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                r1.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                r1.keyPressed(e);
            }
        });
        setFocusable(true);
    }

    /**
     * Comprova el temps transcorregut per actualitzar la puntuació i el nivell.
     * Incrementa la puntuació cada mil·lisegon i augmenta el nivell i la
     * velocitat cada 20 segons.
     */
    private void comprobarTemps() {
        long tempsActual = System.currentTimeMillis();
        //Cada mil·lisegon puja la puntuació
        if (tempsActual - ultimTempsPuntuacio >= 1) {
            puntuacio++;
            ultimTempsPuntuacio = tempsActual;
        }

        // Cada 20 segons augmenta el nivell i la velocitat
        if (tempsActual - ultimTemps >= 20000) {
            nivell++;
            velocitat = VELOCITAT_INICIAL * (1 + incrementVelocitat * nivell);
            ultimTemps = tempsActual;
        }
    }

    /**
     * Actualitza el moviment de la pilota i la raqueta. També comprova el temps
     * per modificar la puntuació i el nivell.
     */
    public void moviment() {
        p1.movimentPilota();
        r1.movimentRaqueta();
        comprobarTemps();
    }

    /**
     * Pinta tots els elements gràfics del joc: fons, pilota, raqueta, així com
     * la puntuació, el nivell i la velocitat actual.
     *
     * @param g Objecte Graphics utilitzat per dibuixar a la pantalla.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //El fons canvia cada 5 nivells
        g2d.drawImage(pista, 0, 0, getWidth(), getHeight(), null);
        if (nivell >= 5) {
            g2d.drawImage(pista2, 0, 0, getWidth(), getHeight(), null);
        }
        if (nivell >= 10) {
            g2d.drawImage(pista3, 0, 0, getWidth(), getHeight(), null);
        }
        p1.paint(g2d);
        r1.paint(g2d);

        //Mostra per pantalla la puntuació, nivell i velocitat actual
        g2d.setColor(Color.magenta);
        g2d.setFont(new Font("Arial", Font.BOLD, 15));
        g2d.drawString(conexio.obtenirTraduccio("SCORE_TEXT", idioma) + " " + getPuntuacio(), 10, 40);
        g2d.drawString(conexio.obtenirTraduccio("LEVEL_TEXT", idioma) + " " + nivell, 10, 60);
        g2d.drawString(conexio.obtenirTraduccio("SPEED_TEXT", idioma) + " " + velocitat, 10, 80);
    }

    /**
     * Finalitza la partida actual. Reprodueix el so de Game Over, desa la
     * puntuació, mostra el diàleg de Game Over i pregunta si l’usuari vol
     * reiniciar o sortir del joc.
     */
    public void gameOver() {
        musica.gameOver();

        conexio.conectar();
        conexio.insertarPuntuacion(this.playerName, this.puntuacio);
        String[][] datos = conexio.obtenerPuntuaciones();
        conexio.desconectar();

        // Mostrar diàleg personalitzat
        DialogoGameOver dialogo = new DialogoGameOver((JFrame) SwingUtilities.getWindowAncestor(this), datos, idioma);
        dialogo.setVisible(true);
    }

    //Getters
    public double getIncrementVelocitat() {
        return incrementVelocitat;
    }

    public double getVelocitat() {
        return velocitat;
    }

    public int getNivell() {
        return nivell;
    }

    public int getVELOCITAT_INICIAL() {
        return VELOCITAT_INICIAL;
    }

    public int getPuntuacio() {
        return puntuacio;
    }

    //Setters
    public void setNivell(int nivell) {
        this.nivell = nivell;
    }

    public void setVelocitat(double velocitat) {
        this.velocitat = velocitat;
    }
}

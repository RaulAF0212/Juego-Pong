package Sonido;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Classe utilitària per gestionar els efectes de so i música del joc Inclou
 * música de fons, efectes de so per a esdeveniments com el rebot de la pilota o
 * el Game Over.
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class Sound {

    private static Clip musicaFondo;
    private static Clip musicaMenu;
    private static Clip clipRebotPilota;
    private static Clip clipGameOver;

    /**
     * Constructor per defecte de Sound
     */
    public Sound() {
    }

    /**
     * Reprodueix la música del menú principal en bucle, S'executa en iniciar el
     * joc abans de començar una partida.
     */
    public void reproducirMusicaMenu() {
        try {
            URL musica = Sound.class.getResource("/resources/sonidos/musicaMenu.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaMenu = AudioSystem.getClip();
                musicaMenu.open(audioIn);
                musicaMenu.loop(Clip.LOOP_CONTINUOUSLY);
                musicaMenu.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música del menú.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reprodueix la música de fons mentre el jugador jugar Atura la música del
     * menú si encara està sonant.
     */
    public void reproducirMusicaFondo() {
        try {
            if (musicaMenu != null && musicaMenu.isRunning()) {
                musicaMenu.stop();
                musicaMenu.close();
            }

            URL musica = Sound.class.getResource("/resources/sonidos/tennisSong.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaFondo = AudioSystem.getClip();
                musicaFondo.open(audioIn);
                musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
                musicaFondo.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música del joc.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reprodueix un efecte de so breu quan la pilota rebota amb la raqueta.
     */
    public void reproducirRebotarPilota() {
        try {
            URL rebotPilota = Sound.class.getResource("/resources/sonidos/ball.wav");
            if (rebotPilota != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(rebotPilota);
                clipRebotPilota = AudioSystem.getClip();
                clipRebotPilota.open(audioIn);
                clipRebotPilota.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de so de rebot de pilota.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Reprodueix el so de Game Over. També ajusta el volum si és possible.
     */
    public void reproducirGameOver() {
        try {
            URL gameOver = Sound.class.getResource("/resources/sonidos/gameOver1.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(gameOver);
            clipGameOver = AudioSystem.getClip();
            clipGameOver.open(audioIn);

            if (clipGameOver.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clipGameOver.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(+6.0f); // Augmenta el volum
            }

            clipGameOver.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Seqüència completa de Game Over: atura la música de fons, reprodueix el
     * so de fi de partida, espera dos segons, i després reprodueix la música
     * final en bucle.
     */
    public void gameOver() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
            musicaFondo.close();
        }

        reproducirGameOver();

        try {
            Thread.sleep(2000); // Espera per donar temps al so de Game Over
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        reproducirMusicaFinal();
    }

    /**
     * Reprodueix la música final del joc en bucle continuat. Aquesta música es
     * reprodueix després del Game Over.
     */
    public void reproducirMusicaFinal() {
        try {
            URL musica = Sound.class.getResource("/resources/sonidos/musicaFinal.wav");
            if (musica != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(musica);
                musicaFondo = AudioSystem.getClip();
                musicaFondo.open(audioIn);
                musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
                musicaFondo.start();
            } else {
                System.err.println("No s'ha trobat l'arxiu de música final.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

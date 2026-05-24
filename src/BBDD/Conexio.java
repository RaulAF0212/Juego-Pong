package BBDD;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import LogicaJoc.Logica;

/**
 * Clase que gestiona la conexio a BBDD, la insercio i obtencio de registres y
 * per fer la desconexio a la BBDD
 *
 * @author Mark, Josep, Raul, Adria i Steveen
 */
public class Conexio {

    private Connection con;
    private final Logica game;
    private static final String URL = "jdbc:mysql://localhost:3306/pong";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Constructor de la clase Conexio
     *
     * @param game pasa l'estat mes recent del joc
     */
    public Conexio(Logica game) {
        this.game = game;
    }

    /**
     * Metode per fer la conexio del projecte a la base de dades que volem
     * conectar
     */
    public void conectar() {
        try {
            con = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión exitosa a la BD");
        } catch (SQLException e) {
            System.err.print("Error conectando a la BD: " + e.getMessage());
        }
    }

    /**
     * Metode per insertar les els valors, com el nom i la puntuacio, a la taula
     * de la BBDD
     *
     * @param nombre nom del jugador
     * @param score puntuacio que ha fet l'usuari
     */
    public void insertarPuntuacion(String nombre, int score) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);  // Deshabilitar auto-commit

            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO puntuacion (nom, score, fecha) VALUES (?, ?, ?)")) {

                pstmt.setString(1, nombre);
                pstmt.setInt(2, score);
                pstmt.setString(3, LocalDateTime.now().format(DATE_FORMATTER));
                pstmt.executeUpdate();

                conn.commit();  // Confirmar canvis 

            } catch (SQLException e) {
                conn.rollback();  // Revertir en cas d'error
                throw e;
            }
        } catch (SQLException e) {
            System.err.print("Error guardando puntuación: " + e.getMessage());

        }
    }

    /**
     * Metode per obtenir els registres que volem, com els 10 primer registres
     *
     * @return retorna una taula amb els 10 primer registres depenent del ordre
     * que volem amb l'instruccio sql feta amb un String
     */
    public String[][] obtenerPuntuaciones() {
        String[][] datos = new String[10][4];
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {

            String sql
                    = "SELECT * FROM ("
                    + "   SELECT nom, score, "
                    + "          DATE_FORMAT(fecha, '%Y-%m-%d %H:%i') as fecha_formateada, "
                    + "          fecha "
                    + //Mantenir la columna original per ordenar
                    "   FROM puntuacion "
                    + "   ORDER BY fecha DESC "
                    + "   LIMIT 20"
                    + ") AS recientes "
                    + "ORDER BY score DESC, fecha DESC "
                    + //Utilitzar la columna original
                    "LIMIT 10";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                int i = 0;
                while (rs.next() && i < 10) {
                    datos[i][0] = rs.getString("nom") != null ? rs.getString("nom") : "Anónimo";
                    datos[i][1] = String.valueOf(rs.getInt("score"));
                    datos[i][2] = rs.getString("fecha_formateada"); // Utilitzar el alies per mostrar
                    i++;
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return datos;
    }

    /**
     * Metode per obtenir les traduccion que volem a consequencia de que
     * l'usuari hagi escollit un idiome o un altre
     *
     * @param clau la referencia a quina traduccio volem accedir
     * @param idioma l'idioma seleccionat
     * @return retorna la traduccio del text que es traduira
     */
    public String obtenirTraduccio(String clau, String idioma) {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD); Statement stmt = conn.createStatement()) {
            String sql
                    = "   SELECT Catala, Castellano, Angles "
                    + "   FROM Traduccio "
                    + "   WHERE Traduccio_Key = '" + clau + "'";

            try (ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    if ("Catala".equals(idioma)) {
                        return rs.getString("Catala");
                    } else if ("Castellano".equals(idioma)) {
                        return rs.getString("Castellano");
                    } else {
                        return rs.getString("Angles");
                    }
                } else {
                    return "Traduccio no trobada";
                }
            } catch (SQLException e) {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    /**
     * Metode per tancar la conexio del projecte a la BBDD
     */
    public void desconectar() {
        try {
            if (con != null) {
                con.close();
            }
            System.out.println("Conexión cerrada");
        } catch (SQLException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}

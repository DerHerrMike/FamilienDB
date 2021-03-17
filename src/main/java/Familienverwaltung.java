import com.mysql.cj.jdbc.MysqlDataSource;
import model.Mitglied;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Familienverwaltung {

    private Connection connection;


    private void initConnection() {

        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setDatabaseName("schwingi");
        dataSource.setUser("root");
        dataSource.setPassword("usbw");
        dataSource.setPort(3307);
        dataSource.setServerName("localhost");
        try {
            Connection connection = dataSource.getConnection();
            System.out.println("Verbindung zu DB hergestellt!");
            // Instanzvariable initialisieren
            this.connection = connection;
        } catch (SQLException ex) {
            // Im Fehlerfall: Fehlermeldung ausgeben
            System.out.println("Fehler bei DB-Connection: " + ex.getMessage());
            ex.printStackTrace();
            // Programm beenden
            System.exit(0);
        }
    }

    private void insertMitglied(Mitglied mitglied) {

        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO familie (vorname, geschlecht, geburtsdatum, svnummer, volljahrig) VALUES (?,?,?,?,?)");
            // werte setzen
            ps.setString(1, mitglied.getVorname());
            ps.setString(2, mitglied.getGeschlecht());
            ps.setString(3,  mitglied.getGeburtsdatum());
            ps.setString(4, mitglied.getSvNummer());
            ps.setBoolean(5, mitglied.isVolljahrig());

            // query
            ps.executeUpdate();
            ps.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private Optional<Mitglied> getMitgliedBySVNr(String svNummer) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT sv-nummer FROM mitglied WHERE sv-nummer =?");
        //query
        try (ps) {
            ps.setString(4, svNummer);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                // Einzelne Werte vom Datensatz holen
                String svN = resultSet.getString("sv-nummer");
                String sex = resultSet.getString("geschlecht");
                String vorname = resultSet.getString("vorname");
                String dob = resultSet.getString("geburtsdatum");
                Boolean vollj = resultSet.getBoolean("volljährig");
                // Daraus ein USer Obejkt erstellen
                Mitglied mitglied = new Mitglied(vorname, sex, svN, vollj,  dob);
                return Optional.of(mitglied);
            }
        }
        ps.close();
        return Optional.empty();
    }

//    private List<Mitglied> getAllMitglieder() throws SQLException{
//
//    }
//


    private void closeConnection() {

        try {
            connection.close();
            System.out.println("Datenbankverbindung geschlossen!");
        } catch (SQLException ex) {
            System.out.println("Fehler dei DB-Connection schließen " + ex.getMessage());
        }
    }


    public void generateMitglied() {

        boolean volljahrig;
        int i;
        for (i=0; i<1; i++){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Vorname: ");
            String vorname = scanner.nextLine();
            System.out.println("Geschlecht: ");
            String sex = scanner.nextLine();
            System.out.println("Geburtsdatum in Format dd.mm.yyyy: ");
            String dob = scanner.nextLine();
            System.out.println("SV-Nummer (4-stellig) eingeben: ");
            String svN = scanner.nextLine();
            System.out.println("Ist das Mitglied volljährig? (1 für Ja, 2 für Nein): ");
            int vj = scanner.nextInt();
            if (vj == 1) {
                volljahrig = true;
            } else {
                volljahrig = false;
            }
            Mitglied mitglied = new Mitglied(vorname, sex, svN, volljahrig, dob);
            insertMitglied(mitglied);
        }

    }

    public static void main(String[] args) {

        Familienverwaltung familienverwaltung = new Familienverwaltung();
        familienverwaltung.initConnection();
        familienverwaltung.generateMitglied();
        familienverwaltung.closeConnection();
      }

}



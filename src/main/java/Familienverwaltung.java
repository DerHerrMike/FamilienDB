import com.mysql.cj.jdbc.MysqlDataSource;
import model.Mitglied;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            ps.setString(3, mitglied.getGeburtsdatum());
            ps.setString(4, mitglied.getSvNummer());
            ps.setBoolean(5, mitglied.isVolljahrig());

            // query
            ps.executeUpdate();
            System.out.println();
            System.out.println("Mitglied in DB hinzugefügt!");
            System.out.println();
            ps.close();
        } catch (SQLException ex) {
            System.out.println("Fehler beim Einfügen! " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Optional<Mitglied> getMitgliedBySVNr(String svNummer) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT svnummer FROM familie WHERE svnummer = ?");
        //query
        try (ps) {
            ps.setString(4, svNummer);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                // Einzelne Werte vom Datensatz holen
                String vorname = resultSet.getString("vorname");
                String sex = resultSet.getString("geschlecht");
                String dob = resultSet.getString("geburtsdatum");
                String svN = resultSet.getString("svnummer");
                Boolean vollj = resultSet.getBoolean("volljahrig");
                // Daraus ein Mitglied Objekt erstellen
                Mitglied mitglied = new Mitglied(vorname, sex, svN, vollj, dob);
                return Optional.of(mitglied);
            }
        }
        ps.close();
        return Optional.empty();
    }

    private List<Mitglied> getAllMitgliederInDB() throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT vorname, geschlecht, geburtsdatum, svnummer, volljahrig FROM familie");
        //query
        ResultSet resultSet = ps.executeQuery();
        // Liste für Rückgabe
        List<Mitglied> result = new ArrayList<>();
        // Zeile für Zeile result abarbeiten solange es ein next gibt
        while (resultSet.next()) {
            // Einzelne Werte vom Datensatz holen
            String vorname = resultSet.getString("vorname");
            String sex = resultSet.getString("geschlecht");
            String dob = resultSet.getString("geburtsdatum");
            String svN = resultSet.getString("svnummer");
            boolean vollj = resultSet.getBoolean("volljahrig");
            // Daraus ein Mitglied Objekt erstellen
            Mitglied mitglied = new Mitglied(vorname, sex, svN, vollj, dob);
            // dieses mitglied in liste result einfügen
            result.add(mitglied);
        }
        ps.close();
        return result;
    }


    private void verwaltung() throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Willkommen in der Familienverwaltung!");
        System.out.println();
        System.out.println("Bitte Auswahl treffen: 1 Mitglied hinzufügen, 2 alle Mitglieder ausgeben, 3 Mitglied über SV-Nummer suchen: ");
        int select = scanner.nextInt();
        switch (select) {
            case 1:
                generateMitglied();
                break;
            case 2:
                try {
                    List<Mitglied> mitglied = getAllMitgliederInDB();
                    for (Mitglied m : mitglied) {
                        print(m);
                        break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Fehler beim Auslesen aller User " + ex.getMessage());
                    break;
                }

            case 3:
                System.out.println("Bitte die SV-Nummer 4-stellig eingeben: ");
                String svNSELECT = scanner.nextLine();
                try {
                    Optional<Mitglied> optionalMitglied = getMitgliedBySVNr(svNSELECT);
                    if (optionalMitglied.isPresent()) {
                        Mitglied mitglied = optionalMitglied.get();
                        print(mitglied);
                        break;
                    } else {
                        System.out.println("Mitglied nicht gefunden!");
                        break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Fehler beim Finden des Mitglieds " + ex.getMessage());
                    break;
                }
            default:
                System.out.println("Keine mögliche Auswahl getroffen. Das Programm wird beendet!");
                System.exit(0);
        }
    }

    private void print(Mitglied mitglied) {
        System.out.printf("%s %s \n", mitglied.getSvNummer(), mitglied.getVorname());
    }

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
        for (i = 0; i < 1; i++) {
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

    public static void main(String[] args) throws SQLException {

        Familienverwaltung familienverwaltung = new Familienverwaltung();
        familienverwaltung.initConnection();
        familienverwaltung.verwaltung();
        familienverwaltung.closeConnection();
    }

}



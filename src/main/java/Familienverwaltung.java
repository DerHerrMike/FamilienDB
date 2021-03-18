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
                    "INSERT INTO familie2 (id, vorname, geschlecht, geburtsdatum, svnummer, volljahrig) VALUES (?,?,?,?,?,?)");
            // werte setzen
            ps.setInt(1, mitglied.getId());
            ps.setString(2, mitglied.getVorname());
            ps.setString(3, mitglied.getGeschlecht());
            ps.setString(4, mitglied.getGeburtsdatum());
            ps.setString(5, mitglied.getSvNummer());
            ps.setInt(6, mitglied.isVolljahrig());

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

    private Optional<Mitglied> getMitgliedByID(int id) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM familie2 WHERE id = ?");
        //query
        try (ps) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                // Einzelne Werte vom Datensatz holen
                int idf = resultSet.getInt("id");
                String vorname = resultSet.getString("vorname");
                String sex = resultSet.getString("geschlecht");
                String dob = resultSet.getString("geburtsdatum");
                String svN = resultSet.getString("svnummer");
                int vollj = resultSet.getInt("volljahrig");
                // Daraus ein Mitglied Objekt erstellen
                Mitglied mitglied = new Mitglied(idf, vorname, sex, dob, svN, vollj);
                return Optional.of(mitglied);
            }
        }
        ps.close();
        return Optional.empty();
    }

    private void changeVollj(int id) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("UPDATE familie2 SET Volljahrig = Volljahrig + 1 WHERE id = ?");
      try (ps){
          ps.setInt(1, id);
          ps.executeUpdate();
          System.out.println("Die Volljährigkeit wurde für ID "+id+" angepasst!");
          System.out.println();
          System.out.println("Das Programm wird beendet.");
          System.exit(0);
      }
    }

    private void deleteMitgliedBySVNr(int sozi) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("DELETE FROM `schwingi`.`familie2` WHERE `familie2`.`SVNummer` = ?");
        try (ps) {
            ps.setInt(1, sozi);
            ps.executeUpdate();
        }
        ps.close();
        System.out.println("Mitglied mit SVNr. " + sozi + " gelöscht!");
        System.out.println();
        System.out.println("Das Programm wird beendet.");
        System.exit(0);
    }


    private List<Mitglied> getAllMitgliederInDB() throws SQLException {

        PreparedStatement ps = connection.prepareStatement("SELECT id, vorname, geschlecht, geburtsdatum, svnummer, volljahrig FROM familie2");
        //query
        ResultSet resultSet = ps.executeQuery();
        // Liste für Rückgabe
        List<Mitglied> result = new ArrayList<>();
        // Zeile für Zeile result abarbeiten solange es ein next gibt
        while (resultSet.next()) {
            // Einzelne Werte vom Datensatz holen
            int id = resultSet.getInt("id");
            String vorname = resultSet.getString("vorname");
            String sex = resultSet.getString("geschlecht");
            String dob = resultSet.getString("geburtsdatum");
            String svN = resultSet.getString("svnummer");
            int vollj = resultSet.getInt("volljahrig");
            // Daraus ein Mitglied Objekt erstellen
            Mitglied mitglied = new Mitglied(id, vorname, sex, dob, svN, vollj);
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
        System.out.println("Bitte Auswahl treffen: 1 Mitglied hinzufügen, 2 Mitglied über ID suchen, 3 alle Mitglieder ausgeben, 4 über SVNr löschen, 5 Volljährigkeit anpassen: ");
        int select = scanner.nextInt();
        scanner.nextLine();
        switch (select) {
            case 1:
                generateMitglied();

                break;
            case 2:

                System.out.println("Bitte die ID eingeben: ");
                int idSELECT = scanner.nextInt();
                scanner.nextLine();
                try {
                    Optional<Mitglied> optionalMitglied = getMitgliedByID(idSELECT);
                    if (optionalMitglied.isPresent()) {
                        Mitglied mitglied = optionalMitglied.get();
                        print(mitglied);
                        System.exit(0);
                    } else {
                        System.out.println("Mitglied nicht gefunden!");
                        System.exit(0);
                    }
                } catch (SQLException ex) {
                    System.out.println("Fehler beim Finden des Mitglieds! " + ex.getMessage());
                    ex.printStackTrace();
                }
                break;
            case 3:
                try {
                    List<Mitglied> mitglied = getAllMitgliederInDB();
                    for (Mitglied m : mitglied) {
                        print(m);
                    }
                    System.exit(0);
                } catch (SQLException ex) {
                    System.out.println("Fehler beim Auslesen aller User " + ex.getMessage());
                }
                break;
            case 4:

                System.out.println("Bitte die SVNummer des zu löschenden Mitglieds eingeben: ");
                int svSelect = scanner.nextInt();
                scanner.nextLine();
                System.out.println("Achtung: Mitglied wird aus DB gelöscht! Fortfahren (j/n): ");
                String delete = scanner.nextLine();
                if (delete.equals("j")) {
                    deleteMitgliedBySVNr(svSelect);
                    break;
                } else {
                    System.out.println("Vorgang abgebrochen!");
                    System.exit(0);
                }

            case 5:

                System.out.println("Gib die ID des Mitglieds ein, dessen Status du ändern willst: ");
                idSELECT = scanner.nextInt();
                scanner.nextLine();
                try {
                    Optional<Mitglied> optionalMitglied = getMitgliedByID(idSELECT);
                    if (optionalMitglied.isPresent()) {
                        changeVollj(idSELECT);
                        break;
                    } else {
                        System.out.println("Mitglied nicht gefunden!");
                        System.exit(0);
                    }
                } catch (SQLException ex) {
                    System.out.println("Fehler beim Finden des Mitglieds! " + ex.getMessage());
                    ex.printStackTrace();
                }


            default:
                System.out.println("Keine mögliche Auswahl getroffen. Das Programm wird beendet!");
                System.exit(0);
        }
    }


    private void print(Mitglied mitglied) {
        System.out.printf("%d %s %s \n", mitglied.getId(), mitglied.getVorname(), mitglied.getSvNummer());
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

        int i;
        for (i = 0; i < 1; i++) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Vorname: ");
            String vorname = scanner.nextLine();
            System.out.println("Geschlecht: ");
            String sex = scanner.nextLine();
            System.out.println("Geburtsdatum in Format dd.mm.yyyy: ");
            String dob = scanner.nextLine();
            System.out.println("SV-Nummer (4-stellig) eingeben: ");
            String svN = scanner.nextLine();
            System.out.println("Ist das Mitglied volljährig? (1 für Ja, 0 für Nein): ");
            int vollj = scanner.nextInt();
            Mitglied mitglied = new Mitglied(id, vorname, sex, dob, svN, vollj);
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



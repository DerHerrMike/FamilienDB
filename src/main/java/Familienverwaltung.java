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
                    "INSERT INTO familie (vorname, geschlecht, geburtsdatum, svNummer, volljahrig) VALUES (?,?,?,?,?)");
            // werte setzen
            ps.setString(1, mitglied.getVorname());
            ps.setString(2, mitglied.getGeschlecht());
            ps.setString(3, mitglied.getSvNummer());
            ps.setBoolean(4, mitglied.isVolljahrig());
            ps.setDate(5, mitglied.getGeburtsdatum());
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
                Date dob = resultSet.getDate("geburtsdatum");
                Boolean vollj = resultSet.getBoolean("volljährig");
                // Daraus ein USer Obejkt erstellen
                Mitglied mitglied = new Mitglied(vorname, sex, svN, vollj, (java.sql.Date) dob);
                return Optional.of(mitglied);
            }
        }
        ps.close();
        return Optional.empty();
    }
}



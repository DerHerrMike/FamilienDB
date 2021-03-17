package model;

import java.sql.Date;

public class Mitglied {

    private String vorname, geschlecht, svNummer;
    private boolean volljahrig;
    private String geburtsdatum;

    public Mitglied(String vorname, String geschlecht, String svNummer, boolean volljahrig, String geburtsdatum) {
        this.vorname = vorname;
        this.geschlecht = geschlecht;
        this.svNummer = svNummer;
        this.volljahrig = volljahrig;
       this.geburtsdatum = geburtsdatum;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getGeschlecht() {
        return geschlecht;
    }

    public void setGeschlecht(String geschlecht) {
        this.geschlecht = geschlecht;
    }

    public String getSvNummer() {
        return svNummer;
    }

    public void setSvNummer(String svNummer) {
        this.svNummer = svNummer;
    }

    public boolean isVolljahrig() {
        return volljahrig;
    }

    public void setVolljahrig(boolean volljahrig) {
        this.volljahrig = volljahrig;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }
}

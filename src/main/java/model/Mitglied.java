package model;

import java.sql.Date;

public class Mitglied {

    private int id;
    private String vorname, geschlecht;
    private String geburtsdatum;
    private String svNummer;
    private int volljahrig;


    public Mitglied(int id, String vorname, String geschlecht, String geburtsdatum, String svNummer,int volljahrig) {
        this.id = id;
        this.vorname = vorname;
        this.geschlecht = geschlecht;
        this.geburtsdatum = geburtsdatum;
        this.svNummer = svNummer;
        this.volljahrig = volljahrig;
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

    public int isVolljahrig() {
        return volljahrig;
    }

    public void setVolljahrig(int volljahrig) {
        this.volljahrig = volljahrig;
    }

    public String getGeburtsdatum() {
        return geburtsdatum;
    }

    public void setGeburtsdatum(String geburtsdatum) {
        this.geburtsdatum = geburtsdatum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVolljahrig() {
        return volljahrig;
    }
}

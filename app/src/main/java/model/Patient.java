package model;

/**
 * Created by Benedikt on 30.05.2015.
 */
public class Patient {

    private String id;
    private String vorname = "";
    private String nachname="";
    private String svnr="";
    private String gebdatum="";
    private String krankenhaus="";
    private String transport="";
    private String prioritaet="";
    private String bewusstsein="";

    public Patient(String id,String vorname, String nachname, String svnr, String gebdatum, String krankenhaus, String transport, String prioritaet, String bewusstsein) {
        this.vorname = vorname;
        this.id = id;
        this.nachname = nachname;
        this.svnr = svnr;
        this.gebdatum = gebdatum;
        this.krankenhaus = krankenhaus;
        this.transport = transport;
        this.prioritaet = prioritaet;
        this.bewusstsein = bewusstsein;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public String getSvnr() {
        return svnr;
    }

    public void setSvnr(String svnr) {
        this.svnr = svnr;
    }

    public String getGebdatum() {
        return gebdatum;
    }

    public void setGebdatum(String gebdatum) {
        this.gebdatum = gebdatum;
    }

    public String getKrankenhaus() {
        return krankenhaus;
    }

    public void setKrankenhaus(String krankenhaus) {
        this.krankenhaus = krankenhaus;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(String prioritaet) {
        this.prioritaet = prioritaet;
    }

    public String getBewusstsein() {
        return bewusstsein;
    }

    public void setBewusstsein(String bewusstsein) {
        this.bewusstsein = bewusstsein;
    }
}

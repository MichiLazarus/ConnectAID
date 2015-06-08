package model;

/**
 * Created by Benedikt on 30.05.2015.
 */
public class Patient {

    private String id;
    private String vorname ;
    private String nachname;
    private String svnr;
    private String gebdatum;
    private String krankenhaus;
    private String prioritaet;
    private boolean bewusstsein, atmung,kreislauf, sauerstoff, intubation, beatmung, blutstillung, pleuradrainage, dringend;


    public Patient(String id, String vorname, String nachname, String svnr, String gebdatum, String krankenhaus, String prioritaet, boolean bewusstsein, boolean atmung, boolean kreislauf, boolean sauerstoff, boolean intubation, boolean beatmung, boolean blutstillung, boolean pleuradrainage, boolean dringend) {
        this.id = id;
        this.vorname = vorname;
        this.nachname = nachname;
        this.svnr = svnr;
        this.gebdatum = gebdatum;
        this.krankenhaus = krankenhaus;
        this.prioritaet = prioritaet;
        this.bewusstsein = bewusstsein;
        this.atmung = atmung;
        this.kreislauf = kreislauf;
        this.sauerstoff = sauerstoff;
        this.intubation = intubation;
        this.beatmung = beatmung;
        this.blutstillung = blutstillung;
        this.pleuradrainage = pleuradrainage;
        this.dringend = dringend;
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

    public String getPrioritaet() {
        return prioritaet;
    }

    public void setPrioritaet(String prioritaet) {
        this.prioritaet = prioritaet;
    }

    public String getBewusstsein() {
        if(this.bewusstsein){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setBewusstsein(boolean bewusstsein) {
        this.bewusstsein = bewusstsein;
    }


    public String getAtmung() {
        if(this.atmung){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setAtmung(boolean atmung) {
        this.atmung = atmung;
    }

    public String getKreislauf() {
        if(this.kreislauf){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setKreislauf(boolean kreislauf) {
        this.kreislauf = kreislauf;
    }

    public String getSauerstoff() {
        if(this.sauerstoff){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setSauerstoff(boolean sauerstoff) {
        this.sauerstoff = sauerstoff;
    }

    public String getIntubation() {
        if(this.intubation){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setIntubation(boolean intubation) {
        this.intubation = intubation;
    }

    public String getBeatmung() {
        if(this.beatmung){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setBeatmung(boolean beatmung) {
        this.beatmung = beatmung;
    }

    public String getBlutstillung() {
        if(this.blutstillung){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setBlutstillung(boolean blutstillung) {
        this.blutstillung = blutstillung;
    }

    public String getPleuradrainage() {
        if(this.pleuradrainage){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setPleuradrainage(boolean pleuradrainage) {
        this.pleuradrainage = pleuradrainage;
    }

    public String getDringend() {
        if(this.dringend){
            return "TRUE";
        }else{
            return "FALSE";
        }
    }

    public void setDringend(boolean dringend) {
        this.dringend = dringend;
    }
}

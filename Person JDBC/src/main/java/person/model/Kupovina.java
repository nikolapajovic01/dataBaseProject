package person.model;

import java.time.LocalDate;

public class Kupovina {
    private LocalDate datumKupovine;
    private String prevoz;
    private String stambeniObjekat; // Pretpostavimo da imate klasu za Stambene Objekte
    private double ukupnaCena;

    // Konstruktor
    public Kupovina(LocalDate datumKupovine, String prevoz, String stambeniObjekat, double ukupnaCena) {
        this.datumKupovine = datumKupovine;
        this.prevoz = prevoz;
        this.stambeniObjekat = stambeniObjekat;
        this.ukupnaCena = ukupnaCena;
    }

    // Getteri
    public LocalDate getDatum_kupovine() {
        return datumKupovine;
    }

    public String getPrevoz() {
        return prevoz;
    }

    public String getStambeniObjekat() {
        return stambeniObjekat;
    }

    public double getUkupnaCena() {
        return ukupnaCena;
    }
}


package person.model;

import java.time.LocalDate;

public class Korisnik {

    private final int korisnikID;

    private String ime;

    private String prezime;

    private String korisnickoIme;

    private String lozinka;

    public Korisnik(int korisnikID, String ime, String prezime, String korisnickoIme, String lozinka) {
        this.korisnikID = korisnikID;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public int getKorisnikID() {
        return korisnikID;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }
}

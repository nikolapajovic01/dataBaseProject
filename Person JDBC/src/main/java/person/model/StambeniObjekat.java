package person.model;

public class StambeniObjekat {
    private int objekatId;
    private int planetId;
    private String naziv_objekta;
    private int kapacitet;
    private double cena;

    public StambeniObjekat(int objekatId, int planetId, String naziv_objekta, int kapacitet, double cena) {
        this.objekatId = objekatId;
        this.planetId = planetId;
        this.naziv_objekta = naziv_objekta;
        this.kapacitet = kapacitet;
        this.cena = cena;
    }

    // Getteri
    public int getObjekatId() {
        return objekatId;
    }

    public int getPlanetId() {
        return planetId;
    }

    public String getNaziv_objekta() {
        return naziv_objekta;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public double getCena() {
        return cena;
    }

}


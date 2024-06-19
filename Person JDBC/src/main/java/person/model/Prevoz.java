package person.model;

public class Prevoz {
    private int prevozId;
    private String tip_prevoza;
    private int kapacitet;
    private double cena;

    // Konstruktor
    public Prevoz(int prevozId, String tip_prevoza, int kapacitet, double cena) {
        this.prevozId = prevozId;
        this.tip_prevoza = tip_prevoza;
        this.kapacitet = kapacitet;
        this.cena = cena;
    }

    // Getteri i setteri
    public int getPrevozId() {
        return prevozId;
    }

    public void setPrevozId(int prevozId) {
        this.prevozId = prevozId;
    }

    public String getTip_prevoza() {
        return tip_prevoza;
    }

    public void setTip_prevoza(String tip_prevoza) {
        this.tip_prevoza = tip_prevoza;
    }

    public int getKapacitet() {
        return kapacitet;
    }

    public void setKapacitet(int kapacitet) {
        this.kapacitet = kapacitet;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    // Metoda za prikaz informacija o prevozu
    @Override
    public String toString() {
        return tip_prevoza;
    }
}


package person.model;

public class Planet {
    private int planet_id;
    private String ime;

    public Planet(int planet_id, String ime) {
        this.planet_id = planet_id;
        this.ime = ime;
    }

    public int getPlanet_id() {
        return planet_id;
    }

    public String getName() {
        return ime;
    }

    @Override
    public String toString() {
        return ime;  // Kada se prikaže u ComboBox-u
    }
}


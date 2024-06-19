package person.model.base;

import person.model.Korisnik;
import person.model.StambeniObjekat;
import person.model.utility.JDBCUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Server {

    public static final Server SERVER = new Server();

    private final List<Korisnik> people = new ArrayList<>();

    private static Korisnik trenutniKorisnik;

    private final List<String> korisnickaImena = new ArrayList<>();

    private Server() {

        this.setPeople(JDBCUtils.selectAllFromKorisnik());
    }

    public List<Korisnik> getPeople() {
        return people;
    }

    public void setPeople(Collection<Korisnik> people) {
        this.people.clear();
        this.people.addAll(people);
    }

    public static Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        Server.trenutniKorisnik = trenutniKorisnik;
    }

    public  int getTrenutniKorisnikID() {
        return trenutniKorisnik != null ? trenutniKorisnik.getKorisnikID() : -1;
    }
}

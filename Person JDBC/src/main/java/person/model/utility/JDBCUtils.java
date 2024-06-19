package person.model.utility;

import person.model.Korisnik;
import person.model.Kupovina;
import person.model.Prevoz;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JDBCUtils {

    public static Connection connection = null;

    public static void connect() {
        Properties properties = new Properties();
        properties.put("user", "root");
        properties.put("password", "");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/zus-database", properties);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




    public static List<Korisnik> selectAllFromKorisnik() {
        List<Korisnik> korisnici = new ArrayList<>();
        String query = "select * from korisnici";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int korisnikID = resultSet.getInt(1);
                String ime = resultSet.getString(2);
                String prezime = resultSet.getString(3);
                String korisnickoIme = resultSet.getString(4);
                String lozinka = resultSet.getString(5);
                Korisnik korisnik = new Korisnik(korisnikID, ime, prezime, korisnickoIme,lozinka);
                korisnici.add(korisnik);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return korisnici;
    }

//    public static List<Korisnik> selectFromKorisnik(String ime, String prezime, String korisnickoIme, String lozinka) {
//        List<Korisnik> oldPeople = selectAllFromPerson();
//        Server.SERVER.setPeople(oldPeople);
//        List<Korisnik> people = new ArrayList<>();
//        for (Korisnik oldPerson : oldPeople) {
//            if (yearOfBirth == null || yearOfBirth.length() != 4) {
//                if (oldPerson.getIme().toLowerCase().contains(firstName.toLowerCase())
//                        && oldPerson.getPrezime().toLowerCase().contains(lastName.toLowerCase()))
//                    people.add(oldPerson);
//                continue;
//            }
//            if (oldPerson.getIme().toLowerCase().contains(firstName.toLowerCase()))
//                people.add(oldPerson);
//        }
//        return people;
//    }

    public static void insertIntoKorisnik(String ime, String prezime, String korisnickoIme, String lozinka, Date datumRodjenja) {
        String query = "insert into korisnici (ime, prezime, korisnicko_ime, lozinka, datum_rodjenja)" +
                "values (?, ?, ?, ?,?)";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            connection.setAutoCommit(false);
            statement.setString(1, ime);
            statement.setString(2, prezime);
            statement.setString(3, korisnickoIme);
            statement.setString(4, lozinka);
            statement.setDate(5, datumRodjenja);
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static boolean authenticate(String username, String password) {
        String query = "SELECT lozinka FROM Korisnici WHERE korisnicko_ime = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("lozinka");
                if (storedPassword.equals(password)) {
                    return true;  // Lozinka se poklapa
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Korisničko ime ne postoji ili lozinka nije tačna
    }


    public static Korisnik getKorisnikByUsername(String username) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM korisnici WHERE korisnicko_ime = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Korisnik(
                        rs.getInt("korisnik_id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getString("korisnicko_ime"),
                        rs.getString("lozinka")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Prevoz getPrevozById(int prevozId) {
        String query = "SELECT * FROM Prevoz WHERE prevoz_id = ?";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query)) {
            stmt.setInt(1, prevozId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("prevoz_id");
                String tipPrevoza = rs.getString("tip_prevoza");
                int kapacitet = rs.getInt("kapacitet");
                double cena = rs.getDouble("cena");
                return new Prevoz(id, tipPrevoza, kapacitet, cena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static List<Kupovina> getPurchasesByUserId(int userId) {
        List<Kupovina> purchases = new ArrayList<>();
        String query = "SELECT k.datum_kupovine, k.ukupna_cena, p.tip_prevoza, s.naziv_objekta " +
                "FROM Kupovine k " +
                "JOIN Prevoz p ON k.prevoz_id = p.prevoz_id " +
                "JOIN stambeni_objekti s ON k.objekat_id = s.objekat_id " +
                "WHERE k.korisnik_id = ?";
        try (Connection conn = JDBCUtils.connection;
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LocalDate date = rs.getDate("datum_kupovine").toLocalDate();
                String tipPrevoza = rs.getString("tip_prevoza");
                String nazivObjekta = rs.getString("naziv_objekta");
                double price = rs.getDouble("ukupna_cena");
                purchases.add(new Kupovina(date, tipPrevoza, nazivObjekta, price));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return purchases;
    }

    public static boolean doesUsernameExist(String username) {
        String query = "SELECT COUNT(*) FROM Korisnici WHERE korisnicko_ime = ?";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Proverava da li postoji barem jedan red
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Ukoliko dođe do greške, pretpostavljamo da korisnik ne postoji
    }










    private JDBCUtils() {

    }

}

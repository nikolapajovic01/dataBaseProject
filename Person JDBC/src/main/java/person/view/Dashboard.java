package person.view;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.model.Planet;
import person.model.Prevoz;
import person.model.StambeniObjekat;
import person.model.base.Server;
import person.model.utility.JDBCUtils;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class Dashboard extends GridPane {

    private ComboBox<Planet> planetComboBox = new ComboBox<>();
    private TableView<StambeniObjekat> housingTable = new TableView<>();
    private Button showHousingButton = new Button("Prikaži stambene objekte");
    private DatePicker datePicker = new DatePicker(LocalDate.now());
    private Button kupiKarteButton = new Button("Kupi kartu");
    private Button pregledButton = new Button("Pregledaj kupovine");
    private ComboBox<Prevoz> prevozComboBox = new ComboBox<>();


    private TextField imeSaputnika;
    private TextField prezimeSaputnika;
    private DatePicker datumRodjenjaSaputnika;






    public Dashboard() {


        this.setVgap(10);
        this.setPadding(new Insets(10));
        this.setHgap(10);

        setupComboBox();
        setupHousingTable();
        setupButtonAction();
        setupPrevozComboBox();

        imeSaputnika = new TextField();
        prezimeSaputnika = new TextField();
        datumRodjenjaSaputnika = new DatePicker();
        datumRodjenjaSaputnika.setPromptText("Datum rođenja");
        datumRodjenjaSaputnika.setValue(LocalDate.now());

        datumRodjenjaSaputnika.setOnAction(event -> {
            LocalDate date = datumRodjenjaSaputnika.getValue();
            System.out.println("Izabrani datum: " + date);
        });



        HBox controls = new HBox(10);
        controls.getChildren().addAll(new Label("Izaberi planetu:"),planetComboBox, showHousingButton);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(new Label("Izaberi polazak: "), datePicker, new Label("Izaberi prevoz:"), prevozComboBox);

        HBox hBox1 = new HBox(10);
        hBox1.getChildren().addAll(new Label("Ime saputnika:"), imeSaputnika, new Label("Prezime saputnika:"), prezimeSaputnika, new Label("Rodjenje:"), datumRodjenjaSaputnika);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(controls, hBox, hBox1, kupiKarteButton);


        pregledButton.setOnAction(e -> {
            PurchaseHistoryView view = new PurchaseHistoryView();
            Scene scene = new Scene(view, 460, 300);
            Stage stage = new Stage();
            stage.setTitle("Pregled Kupovina");
            stage.setScene(scene);
            stage.show();
        });


        this.add(vBox,1,0);
        this.add(housingTable,1,1);
        this.add(pregledButton, 1,2);

//        btnChooseHousing = new Button("Odaberi stambeni objekat");
//        btnChooseHousing.setOnAction(e -> {
//            Planet selectedPlanet = comboBox.getValue();
//            if (selectedPlanet != null) {
//                openHousingSelectionForm();
//            } else {
//                System.out.println("Molimo izaberite planetu pre nego što nastavite.");
//            }
//        });
//
//        btnScheduleDeparture = new Button("Zakaži polazak");
//        btnScheduleDeparture.setOnAction(e -> openDepartureScheduleForm());
//
//        btnViewPurchases = new Button("Pregledaj kupovine");
//        btnViewPurchases.setOnAction(e -> openPurchaseOverviewForm());
//
//        // Dodavanje dugmića u GridPane
//        grid.add(comboBox, 0, 0);
//        grid.add(btnChooseHousing, 1, 0);
//        grid.add(btnScheduleDeparture, 0, 1);
//        grid.add(btnViewPurchases, 1, 1);
//
//
//        // Dodajte dodatne dugmiće za ostale funkcije
//
//        getChildren().addAll(grid);
    }



    private void setupComboBox() {
        planetComboBox.setItems(loadDataFromDatabase()); // Metoda za učitavanje planeta
        planetComboBox.getSelectionModel().selectFirst(); // Automatski selektuje prvu planetu
    }

    private void setupPrevozComboBox(){
        prevozComboBox.setItems(loadPrevozOptions());
        prevozComboBox.getSelectionModel().selectFirst();
    }

    private ObservableList<Prevoz> loadPrevozOptions() {
        ObservableList<Prevoz> prevozi = FXCollections.observableArrayList();
        String query = "SELECT * FROM Prevoz";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Prevoz prevoz = new Prevoz(rs.getInt("prevoz_id"), rs.getString("tip_prevoza"), rs.getInt("kapacitet"), rs.getDouble("cena"));
                prevozi.add(prevoz);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prevozi;
    }


    private void setupHousingTable() {
        TableColumn<StambeniObjekat, String> nameCol = new TableColumn<>("Naziv");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("naziv_objekta"));

        TableColumn<StambeniObjekat, Integer> capacityCol = new TableColumn<>("Kapacitet");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("kapacitet"));

        TableColumn<StambeniObjekat, Double> priceCol = new TableColumn<>("Cena");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        housingTable.getColumns().addAll(nameCol, capacityCol, priceCol);
    }

    private void setupButtonAction() {
        showHousingButton.setOnAction(event -> {
            Planet selectedPlanet = planetComboBox.getValue();
            if (selectedPlanet != null) {
                loadHousingUnits(selectedPlanet.getPlanet_id());
            }
        });
        kupiKarteButton.setOnAction(event -> {
            StambeniObjekat selectedHousing = housingTable.getSelectionModel().getSelectedItem();
            Prevoz prevoz = prevozComboBox.getSelectionModel().getSelectedItem();
            if (selectedHousing != null && datePicker.getValue() != null) {
                buyTicket(selectedHousing, datePicker.getValue(),prevoz);

                registerTravel(getCurrentUserId(),datePicker.getValue(),prevozComboBox.getValue().getPrevozId(),planetComboBox.getValue().getPlanet_id());
                int idPutovanja = registerTravel(getCurrentUserId(),datePicker.getValue(),prevozComboBox.getValue().getPrevozId(),planetComboBox.getValue().getPlanet_id());
                addCompanion(idPutovanja,imeSaputnika.getText(),prezimeSaputnika.getText(), datumRodjenjaSaputnika.getValue(), getCurrentUserId());

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Uspesno ste kupili kartu.", ButtonType.OK);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Morate izabrati stambeni objekat i datum polaska.", ButtonType.OK);
                alert.showAndWait();
                System.out.println("Morate izabrati stambeni objekat i datum polaska.");
            }
        });
    }


    private int getSelectedPlanetId() {
        Planet selectedPlanet = planetComboBox.getValue();  // Dohvata trenutno izabranu stavku
        if (selectedPlanet != null) {
            return selectedPlanet.getPlanet_id();  // Vraća ID izabrane planete
        }
        return -1;  // Vraća -1 ako ništa nije izabrano
    }


    private void openPurchaseOverviewForm() {
        // Logika za otvaranje forme za pregled kupovina
        System.out.println("Otvaranje forme za pregled kupovina.");
        // Ovde biste dodali logiku za stvaranje i prikazivanje forme za pregled
    }

    private void openDepartureScheduleForm() {
        // Logika za otvaranje forme za zakazivanje polaska
        System.out.println("Otvaranje forme za zakazivanje polaska.");
        // Implementacija forme
    }

    private void initializeUI() {
        planetComboBox = new ComboBox<>();
        planetComboBox.setPromptText("Izaberite planetu ili satelit");
        this.getChildren().add(planetComboBox); // Dodavanje ComboBox-a u VBox
    }

//    private ObservableList<Planet> loadDataFromDatabase() {
//        ObservableList<Planet> planets = FXCollections.observableArrayList();
//        String query = "SELECT planet_id, ime FROM planete_i_sateliti WHERE nastanjiv = TRUE";
//        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//            while (rs.next()) {
//                planets.add(new Planet(rs.getInt("planet_id"), rs.getString("ime")));
//            }
//            Platform.runLater(() -> {
//                planetComboBox.setItems(planets);  // Sigurno ažuriranje UI iz JavaFX thread-a
//            });
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Greška prilikom učitavanja podataka: " + e.getMessage());
//        }
//        return planets;
//    }




//    "SELECT planet_id, ime FROM planete_i_sateliti WHERE " +
//            "(udaljenost_od_zvezde_km > 100e6 AND udaljenost_od_zvezde_km < 200e6) AND " +
//            "(min_temperatura_kelvin > 150 AND min_temperatura_kelvin < 250 AND max_temperatura_kelvin > 250 AND max_temperatura_kelvin < 350 AND (max_temperatura_kelvin - min_temperatura_kelvin) <= 120) AND " +
//            "(procent_kiseonika > 15 AND procent_kiseonika < 25 AND (procent_kiseonika + procent_drugog_gasa BETWEEN 90 AND 99)) AND " +
//            "(max_visina_gravitacije_km >= 1000) AND " +
//            "(brzina_orbitiranja_kms > 25 AND brzina_orbitiranja_kms < 35) AND " +
//            "(broj_mladih_smrti <= 20)"

    private ObservableList<Planet> loadDataFromDatabase() {
        ObservableList<Planet> planets = FXCollections.observableArrayList();
        String query = "SELECT p.planet_id, p.ime, COUNT(pm.id_smrti) AS broj_mladih_smrti " +
                "FROM planete_i_sateliti p " +
                "LEFT JOIN preminuli pm ON p.planet_id = pm.planet_id " +
                "AND pm.uzrast < 40 " +
                "AND YEAR(pm.datum_smrti) = YEAR(CURRENT_DATE) - 1 " +
                "AND DATEDIFF(pm.datum_smrti, pm.datum_dolaska) <= 365 " +
                "WHERE (p.udaljenost_od_zvezde_km > 100e6 AND p.udaljenost_od_zvezde_km < 200e6) AND " +
                "(p.min_temperatura_kelvin > 150 AND p.min_temperatura_kelvin < 250 AND p.max_temperatura_kelvin > 250 AND p.max_temperatura_kelvin < 350 AND (p.max_temperatura_kelvin - p.min_temperatura_kelvin) <= 120) AND " +
                "(p.procent_kiseonika > 15 AND p.procent_kiseonika < 25 AND (p.procent_kiseonika + p.procent_drugog_gasa BETWEEN 90 AND 99)) AND " +
                "(p.max_visina_gravitacije_km >= 1000) AND " +
                "(p.brzina_orbitiranja_kms > 25 AND p.brzina_orbitiranja_kms < 35) " +
                "GROUP BY p.planet_id, p.ime " +
                "HAVING COUNT(pm.id_smrti) <= 20";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                planets.add(new Planet(rs.getInt("planet_id"), rs.getString("ime")));
            }
            Platform.runLater(() -> {
                planetComboBox.setItems(planets);  // Sigurno ažuriranje UI iz JavaFX thread-a
            });
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Greška prilikom učitavanja podataka: " + e.getMessage());
        }
        return planets;
    }


    private void loadHousingUnits(int planet_id) {
        ObservableList<StambeniObjekat> stambeniObjekti = FXCollections.observableArrayList();
        String query = "SELECT objekat_id, planet_id, naziv_objekta, kapacitet, cena FROM stambeni_objekti WHERE planet_id = ?";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query)) {
            stmt.setInt(1, planet_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StambeniObjekat unit = new StambeniObjekat(
                        rs.getInt("objekat_id"),
                        rs.getInt("planet_id"),
                        rs.getString("naziv_objekta"),
                        rs.getInt("kapacitet"),
                        rs.getDouble("cena")
                );
                stambeniObjekti.add(unit);
            }
            housingTable.setItems(stambeniObjekti);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Greška prilikom učitavanja stambenih objekata: " + e.getMessage());
        }
    }


    private void buyTicket(StambeniObjekat selectedHousing, LocalDate departureDate, Prevoz prevoz) {

        int korisnikId = getCurrentUserId(); // Metoda koja vraća ID trenutno ulogovanog korisnika
        if (korisnikId != -1) {
            String query = "INSERT INTO kupovine (korisnik_id, objekat_id, datum_kupovine, ukupna_cena, prevoz_id) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query)) {
                stmt.setInt(1, korisnikId);
                stmt.setInt(2, selectedHousing.getObjekatId());
                stmt.setDate(3, java.sql.Date.valueOf(departureDate));
                stmt.setDouble(4, selectedHousing.getCena() + prevoz.getCena());  // Sumiranje cena objekta i prevoza

                stmt.setInt(5, prevoz.getPrevozId());
//                stmt.setDouble(5, selectedHousing.getCena() + prevoz.getCena());  // Sumiranje cena objekta i prevoza
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Kupovina uspešno realizovana!");
                } else {
                    System.out.println("Došlo je do greške prilikom kupovine.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Greška prilikom upisa u bazu: " + e.getMessage());
            }
        } else {
            System.out.println("Greška: Korisnik nije identifikovan.");
        }
    }

    private int registerTravel(int korisnikId, LocalDate datumPolaska, int prevoz, int planeta) {
        String query = "INSERT INTO putovanja (datum_polaska, vreme_polaska, korisnik_id, prevoz_id, planeta) VALUES (?, ?, ?, ?, ?)";
        int travelId = -1;
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setDate(1, Date.valueOf(datePicker.getValue()));
            stmt.setTime(2, Time.valueOf(LocalTime.now()));
            stmt.setInt(3, korisnikId);
            stmt.setInt(4, prevozComboBox.getValue().getPrevozId());
            stmt.setInt(5, planetComboBox.getValue().getPlanet_id());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        travelId = generatedKeys.getInt(1); // Pretpostavlja se da je prvi column ID
                    }
                }
            }

            if (stmt.executeUpdate() > 0) {
                System.out.println("Putovanje uspešno registrovano!");
            } else {
                System.out.println("Došlo je do greške prilikom registracije putovanja.");
            }

            JDBCUtils.connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Greška prilikom upisa u bazu: " + e.getMessage());
        }
        return travelId;
    }



    private void addCompanion(int putovanjeId, String ime, String prezime, LocalDate datumRodjenja, int korisnikId) {
        if (putovanjeId == -1) {
            System.out.println("Nevalidan ID putovanja, saputnik nije dodat.");
            return;
        }

        String query = "INSERT INTO saputnici (putovanje_id, ime, prezime, datum_rodjenja, korisnik_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = JDBCUtils.connection.prepareStatement(query)) {
            stmt.setInt(1, putovanjeId);
            stmt.setString(2, ime);
            stmt.setString(3, prezime);
            stmt.setDate(4, java.sql.Date.valueOf(datumRodjenja));
            stmt.setInt(5,korisnikId);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Saputnik uspešno dodat!");
            } else {
                System.out.println("Došlo je do greške prilikom dodavanja saputnika.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Greška prilikom upisa u bazu: " + e.getMessage());
        }
    }




    private int getCurrentUserId() {
        int userId = Server.SERVER.getTrenutniKorisnikID();
        if (userId != -1) {
            return userId;
        } else {
            System.out.println("User ID nije pronadjen");
        }
        return userId;
    }



}


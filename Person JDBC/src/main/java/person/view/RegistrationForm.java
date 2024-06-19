package person.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import person.model.utility.JDBCUtils;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalDate;

public class RegistrationForm extends Stage {
    public RegistrationForm() {
        this.setTitle("Forma za Registraciju");

        // Kreiranje GridPane layout-a
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Naslov
        Label sceneTitle = new Label("Registracija Korisnika");
        grid.add(sceneTitle, 0, 0, 2, 1);

        // Labela i tekstualno polje za ime
        Label nameLabel = new Label("Ime:");
        grid.add(nameLabel, 0, 1);
        TextField nameTextField = new TextField();
        grid.add(nameTextField, 1, 1);

        // Labela i tekstualno polje za prezime
        Label surnameLabel = new Label("Prezime:");
        grid.add(surnameLabel, 0, 2);
        TextField surnameTextField = new TextField();
        grid.add(surnameTextField, 1, 2);

        // Labela i tekstualno polje za korisničko ime
        Label usernameLabel = new Label("Korisničko ime:");
        grid.add(usernameLabel, 0, 3);
        TextField usernameTextField = new TextField();
        grid.add(usernameTextField, 1, 3);

        // Labela i polje za lozinku
        Label passwordLabel = new Label("Lozinka:");
        grid.add(passwordLabel, 0, 4);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 4);

        Label datumRodjenjaLabel = new Label("Datum rodjenja:");
        grid.add(datumRodjenjaLabel,0,5);
        DatePicker datePickerBirthday = new DatePicker();
        datePickerBirthday.setPromptText("Datum rođenja");
        datePickerBirthday.setValue(LocalDate.now());  // Možete postaviti defaultni datum, na primer današnji datum
        grid.add(datePickerBirthday,1,5);

        datePickerBirthday.setOnAction(event -> {
            LocalDate date = datePickerBirthday.getValue();
            System.out.println("Izabrani datum: " + date);
        });



        Hyperlink loginLink = new Hyperlink("Imate nalog? Ulogujte se.");
        loginLink.setOnAction(e -> {
            LoginForm loginForm = new LoginForm();  // Kreira novi LoginForm
            Scene loginScene = new Scene(loginForm);  // Kreira scenu sa LoginForm
            Stage currentStage = (Stage) loginLink.getScene().getWindow();  // Dohvata trenutni Stage
            currentStage.setScene(loginScene);  // Postavlja novu scenu
            currentStage.setTitle("Login");  // Postavlja naslov prozora (opciono)
            currentStage.show();  // Pokazuje Stage sa novom scenom
        });

        // Dugme za registraciju
        Button btn = new Button("Registruj se");
        btn.setOnAction(e -> {
            // Ovdje ide logika za registraciju korisnika

            String ime = null;
            String prezime = null;
            String korisnickoIme = usernameTextField.getText();
            String lozinka = null;
            Date datumRodjenja = null;
            if (!JDBCUtils.doesUsernameExist(korisnickoIme)){
                System.out.println("Registracija korisnika: " + usernameTextField.getText());
                ime = nameTextField.getText();
                prezime = surnameTextField.getText();
                korisnickoIme = usernameTextField.getText();
                lozinka = passwordField.getText();  // Consider hashing this password
                datumRodjenja = Date.valueOf(datePickerBirthday.getValue());
                LoginForm loginForm = new LoginForm();
                Scene scene = new Scene(loginForm, 300, 200);
                Stage stage = (Stage) this.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
                JDBCUtils.insertIntoKorisnik(ime, prezime, korisnickoIme, lozinka, datumRodjenja);
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Greška u registraciji");
                alert.setHeaderText(null);
                alert.setContentText("Korisničko ime '" + korisnickoIme + "' već postoji. Molimo izaberite drugo korisničko ime.");
                alert.showAndWait();
            }




        });
        grid.add(btn, 1, 6);
        grid.add(loginLink,1,7);


        // Postavljanje scene
        Scene scene = new Scene(grid, 400, 275);
        this.setScene(scene);
    }
}


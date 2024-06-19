package person.view;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import person.model.Korisnik;
import person.model.base.Server;
import person.model.utility.JDBCUtils;

public class LoginForm extends VBox {

    private Button loginButton;
    public LoginForm() {
        super(10);  // Postavljanje VBox-a kao root elementa sa razmakom 10
        this.setPadding(new Insets(20));
        this.setMinSize(300, 200); // Ako želite da postavite minimalnu veličinu za ovaj VBox

        Label usernameLabel = new Label("Korisničko ime:");
        TextField usernameTextField = new TextField();

        Label passwordLabel = new Label("Lozinka:");
        PasswordField passwordTextField = new PasswordField();

        loginButton = new Button("Uloguj se");
        loginButton.setOnAction(e -> handleLogin(usernameTextField.getText(), passwordTextField.getText()));

        Hyperlink loginLink = new Hyperlink("Registrujte se");
        loginLink.setOnAction(e -> {
            RegistrationForm registrationForm = new RegistrationForm();
            registrationForm.show();


            LoginForm loginForm = new LoginForm();
            Scene scene = new Scene(loginForm, 300, 200);
            Stage stage = (Stage) this.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        });

        this.getChildren().addAll(usernameLabel, usernameTextField, passwordLabel, passwordTextField, loginButton, loginLink);
    }

    private void handleLogin(String username, String password) {
        if (JDBCUtils.authenticate(username, password)) {

            Korisnik prijavljeniKorisnik = JDBCUtils.getKorisnikByUsername(username);
            if (prijavljeniKorisnik != null){
                Server.SERVER.setTrenutniKorisnik(prijavljeniKorisnik);
            }
            System.out.println("Prijavljivanje uspešno.");
            Dashboard dashboard = new Dashboard();
            Scene scene = new Scene(dashboard, 1050, 400);
            Stage primaryStage = (Stage) loginButton.getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("Korisnički Dashboard");
            primaryStage.show();
        } else {
            System.out.println("Neuspešno prijavljivanje. Proverite korisničko ime i lozinku.");
            // Opcionalno: prikažite poruku o grešci koristeći Alert
            Alert alert = new Alert(Alert.AlertType.WARNING, "Neispravno korisničko ime ili lozinka!", ButtonType.OK);
            alert.showAndWait();
        }
    }


}

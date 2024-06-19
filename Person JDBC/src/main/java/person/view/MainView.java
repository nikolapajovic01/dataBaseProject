package person.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import person.controller.AddControl;
import person.controller.FilterControl;
import person.model.Korisnik;
import person.model.base.Server;

import java.time.LocalDate;

public class MainView extends Stage {

    private final BorderPane root = new BorderPane();

    private final TableView<Korisnik> tvPeople = new PersonTable(Server.SERVER.getPeople());

    private final TextField tfFirstNameFilter = new TextField();
    private final TextField tfLastNameFilter = new TextField();
    private final TextField tfYearFilter = new TextField();
    private final Button btFilter = new Button("Filter");

    private final TextField tfFirstName = new TextField();
    private final TextField tfLastName = new TextField();
    private final DatePicker dpDateOfBirth = new DatePicker(
            LocalDate.now().minusYears(20));
    private final Button btAdd = new Button("Add new person");

    public MainView() {
        super.setTitle("Person JDBC");

        this.btFilter.setOnAction(new FilterControl(this.tfFirstNameFilter, this.tfLastNameFilter, this.tfYearFilter, this.tvPeople));
        this.btAdd.setOnAction(new AddControl(this.tfFirstName, this.tfLastName, this.dpDateOfBirth, this.tvPeople));

        this.root.setCenter(this.tvPeople);
        this.root.setTop(this.filterBox());
        this.root.setRight(this.addBox());

        Button registrationButton = new Button("Registracija");
        registrationButton.setOnAction(e -> openRegistrationForm());
        this.root.setLeft(registrationButton);


        super.setScene(new Scene(this.root));
    }

    private HBox filterBox() {
        HBox hbox = new HBox(10, new Label("First name:"), this.tfFirstNameFilter,
                                new Label("Last name:"), this.tfLastNameFilter,
                                new Label("Year:"), this.tfYearFilter,
                                this.btFilter);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    private GridPane addBox() {
        GridPane gridPane = new GridPane();
        gridPane.addRow(0, new Label("First name:"), this.tfFirstName);
        gridPane.addRow(1, new Label("Last name:"), this.tfLastName);
        gridPane.addRow(2, new Label("Date of birth:"), this.dpDateOfBirth);
        gridPane.add(this.btAdd, 1, 3);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));
        gridPane.setAlignment(Pos.CENTER);
        return gridPane;
    }

    private void openRegistrationForm() {
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.show();
    }
}

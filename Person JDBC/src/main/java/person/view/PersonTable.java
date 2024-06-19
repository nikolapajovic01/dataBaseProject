package person.view;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import person.model.Korisnik;

import java.time.LocalDate;
import java.util.List;

public class PersonTable extends TableView<Korisnik> {
    public PersonTable(List<Korisnik> values) {
        super(FXCollections.observableArrayList(values));

        TableColumn<Korisnik, Integer> tcPersonId = new TableColumn<>("ID");
        TableColumn<Korisnik, String> tcFirstName = new TableColumn<>("First Name");
        TableColumn<Korisnik, String> tcLastName = new TableColumn<>("Last Name");
        TableColumn<Korisnik, LocalDate> tcDOB = new TableColumn<>("Date of Birth");

        tcPersonId.setCellValueFactory(new PropertyValueFactory<>("personId"));
        tcFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tcDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        super.getColumns().add(tcPersonId);
        super.getColumns().add(tcFirstName);
        super.getColumns().add(tcLastName);
        super.getColumns().add(tcDOB);
    }
}

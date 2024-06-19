package person.view;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import person.model.StambeniObjekat;

public class StambeniObjekatTable extends TableView<StambeniObjekat> {

    public StambeniObjekatTable(ObservableList<StambeniObjekat> observableList) {
        super(observableList);

        TableColumn<StambeniObjekat, String> nameCol = new TableColumn<>("Naziv");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("naziv_objekta"));

        TableColumn<StambeniObjekat, Integer> capacityCol = new TableColumn<>("Kapacitet");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("kapacitet"));

        TableColumn<StambeniObjekat, Double> priceCol = new TableColumn<>("Cena");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("cena"));

        super.getColumns().add(nameCol);
        super.getColumns().add(capacityCol);
        super.getColumns().add(priceCol);

    }
}

package person.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import person.model.Kupovina;
import person.model.base.Server;
import person.model.utility.JDBCUtils;

import java.time.LocalDate;

public class PurchaseHistoryView extends VBox {
    private TableView<Kupovina> table = new TableView<>();

    public PurchaseHistoryView() {
        setupTableView();
        this.getChildren().add(table);
        loadPurchases();
    }

    private void setupTableView() {
        TableColumn<Kupovina, LocalDate> dateColumn = new TableColumn<>("Datum Kupovine");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("datum_kupovine"));

        TableColumn<Kupovina, String> typeColumn = new TableColumn<>("Tip Prevoza");
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("prevoz"));

        TableColumn<Kupovina, String> housingNameColumn = new TableColumn<>("Ime stambenog objekta");
        housingNameColumn.setCellValueFactory(new PropertyValueFactory<>("stambeniObjekat"));

        TableColumn<Kupovina, Double> priceColumn = new TableColumn<>("Cena");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("ukupnaCena"));

        table.getColumns().addAll(dateColumn,housingNameColumn, typeColumn, priceColumn);
    }

    private void loadPurchases() {
        int currentUserId = Server.SERVER.getTrenutniKorisnikID();
        ObservableList<Kupovina> purchases = FXCollections.observableArrayList(
                JDBCUtils.getPurchasesByUserId(currentUserId)
        );
        table.setItems(purchases);
    }


}


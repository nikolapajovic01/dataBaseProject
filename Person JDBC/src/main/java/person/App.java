package person;

import javafx.application.Application;
import javafx.stage.Stage;
import person.model.utility.JDBCUtils;
import person.view.MainView;
import person.view.RegistrationForm;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        JDBCUtils.connect();
        stage = new RegistrationForm();
        stage.show();
    }
}

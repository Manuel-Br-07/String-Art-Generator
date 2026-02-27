import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Collections;

public class ReorderCompactDemo extends Application {

    @Override
    public void start(Stage stage) {

        ObservableList<String> items = FXCollections.observableArrayList(
                "Element 1",
                "Element 2",
                "Element 3",
                "Element 4"
        );

        ListView<String> listView = new ListView<>(items);

        Button up = new Button("▲");
        Button down = new Button("▼");

        // Kompakte Move-Logik
        up.setOnAction(e -> move(listView, -1));
        down.setOnAction(e -> move(listView, 1));

        VBox buttons = new VBox(5, up, down);
        HBox root = new HBox(10, listView, buttons);

        stage.setScene(new Scene(root, 300, 200));
        stage.setTitle("Reihenfolge ändern");
        stage.show();
    }

    // Universelle Verschiebe-Methode
    private void move(ListView<String> list, int direction) {

        int index = list.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        int newIndex = index + direction;

        if (newIndex < 0 || newIndex >= list.getItems().size()) return;

        Collections.swap(list.getItems(), index, newIndex);
        list.getSelectionModel().select(newIndex);
    }

    public static void main(String[] args) {
        launch();
    }
}
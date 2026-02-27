import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class CheckBoxDropdownDemo extends Application {

    @Override
    public void start(Stage stage) {

        // =========================
        // Dropdown erstellen
        // =========================

        MenuButton dropdown = new MenuButton("Farben auswählen");

        CheckMenuItem red = new CheckMenuItem("Rot");
        CheckMenuItem green = new CheckMenuItem("Grün");
        CheckMenuItem blue = new CheckMenuItem("Blau");
        CheckMenuItem yellow = new CheckMenuItem("Gelb");
        CheckMenuItem black = new CheckMenuItem("Schwarz");

        dropdown.getItems().addAll(red, green, blue, yellow, black);

        // =========================
        // Button zum Auslesen
        // =========================

        Button printButton = new Button("Auswahl anzeigen");

        printButton.setOnAction(e -> {

            List<CheckMenuItem> items = List.of(red, green, blue, yellow, black);

            List<String> selected = items.stream()
                    .filter(CheckMenuItem::isSelected)
                    .map(CheckMenuItem::getText)
                    .collect(Collectors.toList());

            System.out.println("=== Gewählte Farben ===");
            selected.forEach(System.out::println);
        });

        VBox root = new VBox(20, dropdown, printButton);
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.setTitle("Dropdown mit Checkboxen");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
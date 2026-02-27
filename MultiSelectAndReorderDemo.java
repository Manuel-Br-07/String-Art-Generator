import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Collections;

public class MultiSelectAndReorderDemo extends Application {

    @Override
    public void start(Stage stage) {

        // =========================
        // 1️⃣ Mehrfachauswahl Liste
        // =========================

        ListView<String> multiSelectList = new ListView<>();
        multiSelectList.getItems().addAll(
                "Rot", "Grün", "Blau", "Gelb", "Schwarz"
        );

        multiSelectList.getSelectionModel()
                       .setSelectionMode(SelectionMode.MULTIPLE);

        multiSelectList.setPrefHeight(120);


        // =========================
        // 2️⃣ Sortierbare Liste
        // =========================

        ObservableList<String> orderItems = FXCollections.observableArrayList(
                "Element 1",
                "Element 2",
                "Element 3",
                "Element 4"
        );

        ListView<String> orderList = new ListView<>(orderItems);
        orderList.setPrefHeight(150);


        // ▲ Button
        Button btnUp = new Button("▲");
        btnUp.setOnAction(e -> {
            int index = orderList.getSelectionModel().getSelectedIndex();
            if (index > 0) {
                Collections.swap(orderItems, index, index - 1);
                orderList.getSelectionModel().select(index - 1);
            }
        });

        // ▼ Button
        Button btnDown = new Button("▼");
        btnDown.setOnAction(e -> {
            int index = orderList.getSelectionModel().getSelectedIndex();
            if (index < orderItems.size() - 1 && index >= 0) {
                Collections.swap(orderItems, index, index + 1);
                orderList.getSelectionModel().select(index + 1);
            }
        });

        VBox buttonBox = new VBox(10, btnUp, btnDown);
        buttonBox.setPadding(new Insets(10));

        HBox reorderBox = new HBox(10, orderList, buttonBox);


        // =========================
        // 3️⃣ Ergebnis ausgeben
        // =========================

        Button printButton = new Button("Ausgabe anzeigen");
        printButton.setOnAction(e -> {

            System.out.println("=== Mehrfachauswahl ===");
            for (String s : multiSelectList.getSelectionModel().getSelectedItems()) {
                System.out.println(s);
            }

            System.out.println("=== Reihenfolge ===");
            for (String s : orderList.getItems()) {
                System.out.println(s);
            }
        });


        // =========================
        // Layout
        // =========================

        VBox root = new VBox(20,
                new Label("Mehrfachauswahl:"),
                multiSelectList,
                new Separator(),
                new Label("Reihenfolge ändern:"),
                reorderBox,
                printButton
        );

        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.setTitle("JavaFX Mehrfachauswahl & Sortierung");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
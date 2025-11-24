import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ParameterTestGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10); // 10px Abstand zwischen Elementen
        root.setPadding(new Insets(10));

        // ----------------- Abschnitt 1 -----------------
        Label section1 = new Label("---- Abschnitt 1 ----");
        root.getChildren().add(section1);

        DoubleProperty param1 = new SimpleDoubleProperty(50);
        Label label1 = new Label();
        label1.textProperty().bind(param1.asString("Parameter 1: %.2f"));

        Slider slider1 = new Slider(0, 100, 50);
        slider1.setShowTickLabels(true);
        slider1.setShowTickMarks(true);
        slider1.valueProperty().bindBidirectional(param1);

        root.getChildren().addAll(label1, slider1);

        // Leerzeile für Übersicht
        root.getChildren().add(new Label(""));

        // ----------------- Abschnitt 2 -----------------
        Label section2 = new Label("---- Abschnitt 2 ----");
        root.getChildren().add(section2);

        DoubleProperty param2 = new SimpleDoubleProperty(0.5);
        Label label2 = new Label();
        label2.textProperty().bind(param2.asString("Parameter 2: %.2f"));

        Slider slider2 = new Slider(0, 1, 0.5);
        slider2.setShowTickLabels(true);
        slider2.setShowTickMarks(true);
        slider2.setBlockIncrement(0.01);
        slider2.valueProperty().bindBidirectional(param2);

        root.getChildren().addAll(label2, slider2);

        // ----------------- Abschnitt 3 -----------------
        Label section3 = new Label("---- Abschnitt 3 ----");
        root.getChildren().add(section3);

        DoubleProperty param3 = new SimpleDoubleProperty(10);
        Label label3 = new Label();
        label3.textProperty().bind(param3.asString("Parameter 3: %.0f"));

        Slider slider3 = new Slider(0, 20, 10);
        slider3.setShowTickLabels(true);
        slider3.setShowTickMarks(true);
        slider3.valueProperty().bindBidirectional(param3);

        root.getChildren().addAll(label3, slider3);

        // ----------------- Szene anzeigen -----------------
        Scene scene = new Scene(root, 350, 400);
        primaryStage.setTitle("Parameter Test GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

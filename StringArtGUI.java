import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class StringArtGUI extends Application {

    @Override
    public void start(Stage stage) {
        // Navigation bar
        VBox nav = new VBox(10);
        nav.setPadding(new Insets(10));
        nav.setPrefWidth(150);
        nav.getChildren().addAll(
                new Button("Bild"),
                new Button("StringArt"),
                new Button("G-Code"),
                new Button("Einstellungen")
        );

        // Image page preview (heatmap placeholder)
        ImageView heatmapPreview = new ImageView();
        heatmapPreview.setStyle("-fx-background-color: #222; -fx-min-width: 400; -fx-min-height: 300;");

        VBox imageControls = new VBox(10,
                new Label("Helligkeit:"),
                new Slider(0, 1, 0.5),
                new Label("Kontrast:"),
                new Slider(0, 2, 1),
                new Button("Normalisieren"),
                new Button("Importieren")
        );
        imageControls.setPadding(new Insets(10));

        BorderPane imagePage = new BorderPane();
        imagePage.setCenter(heatmapPreview);
        imagePage.setRight(imageControls);

        // StringArt page preview (Canvas placeholder)
        Canvas stringArtCanvas = new Canvas(400, 300);
        GraphicsContext gc = stringArtCanvas.getGraphicsContext2D();
        gc.strokeText("StringArt Vorschau", 150, 150);

        VBox stringArtControls = new VBox(10,
                new Label("Nägel:"), new Spinner<>(10, 400, 200),
                new Label("Iterationen:"), new Spinner<>(100, 10000, 1000),
                new Label("Maximale Helligkeit:"), new Slider(0, 1, 0.5),
                new Button("Generieren")
        );
        stringArtControls.setPadding(new Insets(10));

        BorderPane stringArtPage = new BorderPane();
        stringArtPage.setCenter(stringArtCanvas);
        stringArtPage.setRight(stringArtControls);

        // G-Code page
        TextArea gcodeArea = new TextArea();
        gcodeArea.setPromptText("Hier wird der G-Code erscheinen...");
        VBox gcodeControls = new VBox(10,
                new Label("Feedrate:"), new Spinner<>(100, 5000, 1000),
                new Label("Spindeldrehzahl:"), new Spinner<>(0, 20000, 8000),
                new Button("G-Code generieren"),
                new Button("Exportieren")
        );
        gcodeControls.setPadding(new Insets(10));

        BorderPane gcodePage = new BorderPane();
        gcodePage.setCenter(gcodeArea);
        gcodePage.setRight(gcodeControls);

        // TabPane for switching pages
        TabPane tabs = new TabPane();
        tabs.getTabs().addAll(
                new Tab("Bild", imagePage),
                new Tab("StringArt", stringArtPage),
                new Tab("G-Code", gcodePage)
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // Layout root
        BorderPane root = new BorderPane();
        root.setLeft(nav);
        root.setCenter(tabs);

        // Scene
        Scene scene = new Scene(root, 900, 600);
        stage.setTitle("StringArt Studio (Prototype)");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

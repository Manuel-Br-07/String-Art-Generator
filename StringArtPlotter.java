import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Ein einfacher JavaFX-Plotter für StringArt-Debug-Zwecke.
 * Du kannst ihn aus deiner Hauptklasse (z.B. ImageToArray) aufrufen:
 *
 *    StringArtPlotter.init(600, 600);
 *    StringArtPlotter.addLine(100, 100, 300, 400);
 *    StringArtPlotter.addLine(200, 500, 400, 100);
 *    StringArtPlotter.show();
 */
public class StringArtPlotter extends Application {

    private static double width = 510;
    private static double height = 510;
    private static final List<double[]> lines = new ArrayList<>();
    private static GraphicsContext gc;
    private static Stage stage;
    private static boolean started = false;

    // Wird von JavaFX aufgerufen
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;

        Canvas canvas = new Canvas(width, height);
        gc = canvas.getGraphicsContext2D();

        // Hintergrund
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, width, height);

        stage.setScene(new Scene(new StackPane(canvas), width, height));
        stage.setTitle("StringArt Debug-Vorschau");
        stage.show();

        redraw();
    }

    /** Initialisiert das Fenster mit gewünschter Größe */
    public static void init(double w, double h) {
        width = w;
        height = h;

        // JavaFX nur einmal starten
        if (!started)
        {
            started = true;
            Thread fxThread = new Thread(() -> Application.launch(StringArtPlotter.class));
            fxThread.setDaemon(true);
            fxThread.start();
        }

        // Kleine Pause, damit FX starten kann
        try { Thread.sleep(500); } catch (InterruptedException ignored) {}
    }

    /** Fügt eine Linie hinzu (Start- und Endkoordinaten in Pixeln) */
    public static void addLine(double x1, double y1, double x2, double y2) {
        lines.add(new double[]{x1, y1, x2, y2});
        redraw();
    }

    /** Löscht alle Linien */
    public static void clear() {
        lines.clear();
        redraw();
    }

    /** Zeichnet alle Linien neu */
    private static void redraw() {
        if (!(gc == null))
        {
            gc.setFill(Color.WHITE);
            gc.fillRect(0, 0, width, height);

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(0.8);

            for (int i = 0; i < lines.size(); i++) {
                double[] l = lines.get(i);
                gc.strokeLine(l[0], l[1], l[2], l[3]);
            }

            System.out.println("Line Drawn");
        }
    }

    /** Zeigt das Fenster (erneut) */
    public static void show() {
        if (stage != null) Platform.runLater(() -> stage.show());
    }
}

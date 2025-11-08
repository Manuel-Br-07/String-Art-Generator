import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LargeHeatmapExample extends Application {

    private static final int WIDTH = 2000;
    private static final int HEIGHT = 2000;

    @Override
    public void start(Stage stage) {
        double[][] data = generateData(WIDTH, HEIGHT);

        long startTime = System.currentTimeMillis();
        WritableImage heatmap = createHeatmapImage(data);
        long elapsed = System.currentTimeMillis() - startTime;

        System.out.println("Heatmap erzeugt in " + elapsed + " ms");

        ImageView view = new ImageView(heatmap);
        view.setFitWidth(800);  // kleiner anzeigen (skalierbar)
        view.setPreserveRatio(true);

        stage.setScene(new Scene(new StackPane(view), 900, 900));
        stage.setTitle("Heatmap (2000x2000)");
        stage.show();
    }

    private WritableImage createHeatmapImage(double[][] data) {
        int h = data.length;
        int w = data[0].length;
        WritableImage img = new WritableImage(w, h);
        PixelWriter pw = img.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double v = clamp(data[y][x]);
                Color c = getColorForValue(v);
                pw.setColor(x, y, c);
            }
        }
        return img;
    }

    // Beispielhafte Farbskala: 0 → Blau, 0.5 → Grün, 1 → Rot
    private Color getColorForValue(double v) {
        if (v < 0.5) {
            return Color.color(0, v * 2, 1 - v * 2); // blau → grün
        } else {
            return Color.color((v - 0.5) * 2, 1 - (v - 0.5) * 2, 0); // grün → rot
        }
    }

    private double clamp(double v) {
        return Math.max(0, Math.min(1, v));
    }

    private double[][] generateData(int w, int h) {
        double[][] arr = new double[h][w];
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double dx = x - w / 2.0;
                double dy = y - h / 2.0;
                arr[y][x] = Math.exp(-(dx * dx + dy * dy) / (2 * 300 * 300));
            }
        }
        return arr;
    }

    public static void main(String[] args) {
        launch();
    }
}

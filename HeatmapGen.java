import javafx.scene.paint.Color;
import javafx.scene.image.WritableImage;
import javafx.scene.image.PixelWriter;
/**
 * Beschreiben Sie hier die Klasse HeatmapGen.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class HeatmapGen
{
    /**
     * Konstruktor für Objekte der Klasse HeatmapGen
     */
    public HeatmapGen()
    { 
    }

    public WritableImage createHeatmap(double[][][] data)
    {
        int h = data.length;
        int w = data[0].length;
        WritableImage img = new WritableImage(w, h);
        PixelWriter pw = img.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double r = data[y][x][0];
                double g = data[y][x][1];
                double b = data[y][x][2];
                Color c;
                if(r == -1)
                {
                    c = Color.color(1, 0, 0);
                }
                else
                {
                    r = Math.max(0, Math.min(1, r));
                    g = Math.max(0, Math.min(1, g));
                    b = Math.max(0, Math.min(1, b));
                    c = Color.color(r, g, b);
                }
                pw.setColor(x, y, c);
            }
        }
        return img;
    }
}

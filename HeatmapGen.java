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

    public WritableImage createHeatmap(double[][] data)
    {
        int h = data.length;
        int w = data[0].length;
        WritableImage img = new WritableImage(w, h);
        PixelWriter pw = img.getPixelWriter();

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                double v = data[y][x];
                Color c;
                if(v == -1)
                {
                    c = Color.color(1, 0, 0);
                }
                else
                {
                    v = Math.max(0, Math.min(1, v));
                    c = Color.color(v, v, v);
                }
                pw.setColor(x, y, c);
            }
        }
        return img;
    }
}

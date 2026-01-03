import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Beschreiben Sie hier die Klasse ImageToArray.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class ImageToArray
{
    private Data data;
    private Queue<int[]> lineOrder;
    private StringArtPlotter stringArtPlotter;

    //immageToArray
    private int width;
    private int height;
    private double[][] bildArray;
    private String  dateiName;

    //circularMask
    private double centerX;
    private double centerY;
    private double radius;

    /**
     * Konstruktor für Objekte der Klasse ImageToArray
     */
    public ImageToArray(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter)
    {
        data = pData;
        lineOrder = pLineOrder;
        stringArtPlotter = pStringArtPlotter;
    }

    public void main()
    {
        // ------------------ GET ------------------
        // width = data.getWidth();
        // height = data.getHeight();
        // bildArray = data.getBildArray();
        dateiName = data.getDateiName();

        centerX = data.getCenterX();
        centerY = data.getCenterY();
        radius = data.getRadius();

        // ------------------ BEARBEITUNG ------------------

        imageToArray();

        // ------------------ SET ------------------
        data.setWidth(width);
        data.setHeight(height);
        data.setBildArray(bildArray);

        data.setCenterX(centerX);
        data.setCenterY(centerY);
        data.setRadius(radius);
    }

    // ---------------- Bild einlesen ----------------
    public void imageToArray()
    {
        try {
            // 1. Bild laden
            BufferedImage image = ImageIO.read(new File(dateiName));

            width = image.getWidth();
            height = image.getHeight();

            // 2. Array für Grauwerte (zwischen 0 und 1)
            bildArray = new double[height][width];

            // 3. Pixel auslesen
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    int rgb = image.getRGB(x, y);

                    // 4. Farbkanäle extrahieren
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    // 5. Grau berechnen (Durchschnitt oder Luminanz)
                    double gray = (0.299 * r + 0.587 * g + 0.114 * b);
                    // System.out.println("" + gray);

                    if (gray > 255)
                    {
                        gray = 255;
                    }
                    else if (gray < 0)
                    {
                        gray = 0;
                    }

                    // 6. Normalisieren auf 0–1
                    bildArray[y][x] = gray / 255;
                    // if(bildArray[y][x] < 0.8)
                    // {
                    // bildArray[y][x] = 0.6;
                    // }
                    // Bildbearbeitung
                    // bildArray[y][x] = 1 - Math.min(1.0, Math.max(0.0, (1 - bildArray[y][x]) * 1.4));
                }
            }

            System.out.println("Breite: " + width + ", Höhe: " + height);
            System.out.println("Pixelwert (0,0): " + bildArray[0][0]);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        circularMask();
    }

    // ---------------- Kreis-Maske ----------------
    public void circularMask()
    {
        // Mittelpunkt bestimmen
        centerX = width / 2.0;
        centerY = height / 2.0;

        // Radius = halbe kürzeste Seite
        radius = Math.min(width, height) / 2.0;

        // Alle Pixel durchgehen
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > radius)
                {
                    bildArray[y][x] = -1; // Pixel außerhalb des Kreises
                }
            }
        }
        
        // Console Print
        // for (int y = 0; y < height; y++)
        // {
            // for (int x = 0; x < width; x++)
            // {
                // if (bildArray[y][x] >= 0)
                // {
                    // if (bildArray[y][x] > 0.5)
                    // {
                        // System.out.print(" ");
                    // }
                    // else
                    // {
                        // System.out.print("█");
                    // }
                // }
                // else
                // {
                    // System.out.print("X");
                    // if (bildArray[y][x] >= 0)
                    // {
                        // System.out.print("T");
                    // }
                // }
            // }
            // System.out.println();
        // }

        // StringArtPlotter.init(width, height);

    }
    
    public void imageProcessing()
    {
        
    }
}

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageToArray {
    private int width = 0;
    private int height = 0;
    private double[][] bildArray;
    public void immageToArray(String DateiName) {

        try {
            // 1. Bild laden
            BufferedImage image = ImageIO.read(new File(DateiName));

            width = image.getWidth();
            height = image.getHeight();

            // 2. Array für Grauwerte (zwischen 0 und 1)
            bildArray = new double[height][width];

            // 3. Pixel auslesen
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);

                    // 4. Farbkanäle extrahieren
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    // 5. Grau berechnen (Durchschnitt oder Luminanz)
                    double gray = (0.299*r + 0.587*g + 0.114*b);

                    // 6. Normalisieren auf 0–1
                    bildArray[y][x] = gray / 255;

                    if(x == 0 && y== 0)
                    {
                        System.out.println("R (" + x + "," + y + "): " + r);
                        System.out.println("G (" + x + "," + y + "): " + g);
                        System.out.println("B (" + x + "," + y + "): " + b);
                        System.out.println("Gray (" + x + "," + y + "): " + bildArray[y][x]);
                    }
                    if(x == 310 && y== 350)
                    {
                        System.out.println("R (" + x + "," + y + "): " + r);
                        System.out.println("G (" + x + "," + y + "): " + g);
                        System.out.println("B (" + x + "," + y + "): " + b);
                        System.out.println("Gray (" + x + "," + y + "): " + bildArray[y][x]);
                    }

                    if(x == 270 && y== 300)
                    {
                        System.out.println("R (" + x + "," + y + "): " + r);
                        System.out.println("G (" + x + "," + y + "): " + g);
                        System.out.println("B (" + x + "," + y + "): " + b);
                        System.out.println("Gray (" + x + "," + y + "): " + bildArray[y][x]);
                    }
                }
            }

            // 7. Beispielausgabe
            System.out.println("Breite: " + width + ", Höhe: " + height);
            System.out.println("Pixelwert (0,0): " + bildArray[0][0]);
            System.out.println("Pixelwert (300,300): " + bildArray[300][300]);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void maskCircleInPlace() {

        // Mittelpunkt bestimmen
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        // Radius = halbe kürzeste Seite
        double radius = Math.min(width, height) / 2.0;

        // Alle Pixel durchgehen
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - centerX;
                double dy = y - centerY;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > radius) {
                    bildArray[y][x] = -1; // Pixel außerhalb des Kreises
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(bildArray[y][x]>=0)
                {
                    if(bildArray[y][x]> 0.5)
                    {
                        System.out.print(" ");
                    }
                    else
                    {
                        System.out.print("█");
                    }
                }
                else
                {
                System.out.print("X");
                }
            }
            System.out.println();
        }
    }

}

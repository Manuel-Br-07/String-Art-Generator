import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main {
    private int width = 0;
    private int height = 0;
    private double[][] bildArray;

    private double centerX;
    private double centerY;
    private double radius;

    private int nails = 0;
    private double diameter = 0;
    private double mmProPixel = 0;
    private double[][] nailCoords;

    double lineWidth = 2;

    private StringArtPlotter stringArtPlotter = new StringArtPlotter();

    // Queue für Linienreihenfolge
    private Queue<int[]> lineOrder = new Queue();

    // ---------------- Bild einlesen ----------------
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
                    double gray = (0.299 * r + 0.587 * g + 0.114 * b);
                    // System.out.println("" + gray);

                    if (gray > 255) {
                        gray = 255;
                    } else if (gray < 0) {
                        gray = 0;
                    }

                    // 6. Normalisieren auf 0–1
                    bildArray[y][x] = gray / 255;
                    bildArray[y][x] = 1 - Math.min(1.0, Math.max(0.0, (1 - bildArray[y][x]) * 1.2));
                }
            }

            System.out.println("Breite: " + width + ", Höhe: " + height);
            System.out.println("Pixelwert (0,0): " + bildArray[0][0]);
            System.out.println("Pixelwert (300,300): " + bildArray[300][300]);

        } catch (Exception e) {
            e.printStackTrace();
        }
        circularMask();
    }

    // ---------------- Kreis-Maske ----------------
    public void circularMask() {
        // Mittelpunkt bestimmen
        centerX = width / 2.0;
        centerY = height / 2.0;

        // Radius = halbe kürzeste Seite
        radius = Math.min(width, height) / 2.0;

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
                if (bildArray[y][x] >= 0) {
                    if (bildArray[y][x] > 0.5) {
                        System.out.print(" ");
                    } else {
                        System.out.print("█");
                    }
                } else {
                    System.out.print("X");
                    if (bildArray[y][x] >= 0) {
                        System.out.print("T");
                    }
                }
            }
            System.out.println();
        }

        StringArtPlotter.init(width, height);

    }

    // ---------------- Nägel ----------------
    public void nailPositions(int pNails) {
        nails = pNails;

        nailCoords = new double[nails][2];
        for (int i = 0; i < nails; i++) {
            double angle = 2 * Math.PI * i / nails;
            nailCoords[i][0] = centerX + radius * Math.cos(angle);
            nailCoords[i][1] = centerY + radius * Math.sin(angle);

            System.out.println("" + i + " W " + angle);
            System.out.println("" + i + "X" + nailCoords[i][0]);
            System.out.println("" + i + "Y" + nailCoords[i][1]);

            if (i >= 1) {
                StringArtPlotter.addLine(nailCoords[i - 1][0], nailCoords[i - 1][1], nailCoords[i][0],
                    nailCoords[i][1]);
                System.out.println("xmin " + nailCoords[i - 1][0] + " ymin " + nailCoords[i - 1][1] + " xmax "
                    + nailCoords[i][0] + " ymax " + nailCoords[i][1]);
            }
        }
    }

    public int stringArtGenerator() {
        int startNail = 0;
        int iterations = 0;
        int[] linePos;
        double endpoint = averageColour() + (1 - averageColour()) / 10 * 9;
        System.out.println("Endpoint " + endpoint);

        while (averageColour() <= endpoint && iterations < 50000) {
            double bestScore = 0;
            int bestEndNail = -1;
            for (int endNail = 0; endNail < nails; endNail++) {
                if (endNail != startNail)// gleiche Nagel überspringen
                {
                    // Berechne Score für Linie von startNail zu endNail
                    double score = calculateLineScore(startNail, endNail);

                    // System.out.println("score: " + score);
                    // System.out.println("length: " + length);

                    if (score > bestScore) {
                        bestScore = score;
                        bestEndNail = endNail;
                    }
                }
            }

            // System.out.println("xmin " + nailCoords[startNail][0] + " ymin " +
            // nailCoords[startNail][1] + " xmax " + nailCoords[bestEndNail][0] + " ymax " +
            // nailCoords[bestEndNail][1]);
            // System.out.println("Start " + startNail + " Ende " + bestEndNail);
            if (bestEndNail != -1) {
                StringArtPlotter.addLine(nailCoords[startNail][0], nailCoords[startNail][1], nailCoords[bestEndNail][0], nailCoords[bestEndNail][1]);
                StringArtPlotter.show();

                linePos = new int[]{startNail, bestEndNail};
                lineOrder.enqueue(linePos);

                lightenLine(startNail, bestEndNail, 0.2);
                startNail = bestEndNail;
                iterations++;
                System.out.println("iterations " + iterations + " average " + averageColour());
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bildArray[y][x] >= 0) {
                    if (bildArray[y][x] > 0.5) {
                        System.out.print(" ");
                    } else {
                        System.out.print("█");
                    }
                } else {
                    System.out.print("X");
                    if (bildArray[y][x] >= 0) {
                        System.out.print("T");
                    }
                }
            }
            System.out.println();
        }
        return iterations;
    }

    public double calculateLineScore(int startNail, int endNail) {
        // Reelle Start- und Endkoordinaten
        double startX = nailCoords[startNail][0];
        double startY = nailCoords[startNail][1];
        double endX = nailCoords[endNail][0];
        double endY = nailCoords[endNail][1];

        double dx = endX - startX;
        double dy = endY - startY;

        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));
        double xIncrement = dx / steps;
        double yIncrement = dy / steps;

        double x = startX;
        double y = startY;

        double score = 0.0;
        int countedPoints = 0;

        for (int i = 0; i <= steps; i++) {
            int px = (int) Math.round(x);
            int py = (int) Math.round(y);

            if (px >= 0 && py >= 0 && px < bildArray[0].length && py < bildArray.length) {
                score += 1.0 - bildArray[py][px];
                countedPoints++;
            }

            x += xIncrement;
            y += yIncrement;
        }

        return countedPoints > 0 ? score / countedPoints : 0.0;
    }

    public void lightenLine(int startNail, int endNail, double increment) {
        // Reelle Start- und Endkoordinaten
        double startX = nailCoords[startNail][0];
        double startY = nailCoords[startNail][1];
        double endX = nailCoords[endNail][0];
        double endY = nailCoords[endNail][1];

        double dx = endX - startX;
        double dy = endY - startY;

        int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));
        double xIncrement = dx / steps;
        double yIncrement = dy / steps;

        double x = startX;
        double y = startY;

        for (int i = 0; i <= steps; i++) {
            int px = (int) Math.round(x);
            int py = (int) Math.round(y);

            if (px >= 0 && py >= 0 && px < bildArray[0].length && py < bildArray.length) {
                bildArray[py][px] += increment;
                if (bildArray[py][px] > 1) {
                    bildArray[py][px] = 1;
                }
            }

            x += xIncrement;
            y += yIncrement;
        }
    }

    // public void lightenLine(int startNail, int endNail, double increment) {
        // double startX = nailCoords[startNail][0];
        // double startY = nailCoords[startNail][1];
        // double endX = nailCoords[endNail][0];
        // double endY = nailCoords[endNail][1];

        // double dx = endX - startX;
        // double dy = endY - startY;

        // int steps = (int) Math.max(Math.abs(dx), Math.abs(dy));
        // double xIncrement = dx / steps;
        // double yIncrement = dy / steps;

        // double x = startX;
        // double y = startY;

        // int halfWidth = (int) lineWidth / 2;

        // // Verwende ein Set, um bereits bearbeitete Pixel zu merken
        // boolean[][] visited = new boolean[bildArray.length][bildArray[0].length];

        // for (int i = 0; i <= steps; i++) {
            // int px = (int) Math.round(x);
            // int py = (int) Math.round(y);

            // for (int wx = -halfWidth; wx <= halfWidth; wx++) {
                // for (int wy = -halfWidth; wy <= halfWidth; wy++) {
                    // int nx = px + wx;
                    // int ny = py + wy;

                    // if (nx >= 0 && ny >= 0 && nx < bildArray[0].length && ny < bildArray.length) {
                        // if (!visited[ny][nx]) {
                            // // Abstand zum Linienmittelpunkt
                            // double distance = Math.sqrt(wx * wx + wy * wy);
                            // // Intensität abnehmen: trapezförmig
                            // double factor = Math.max(0, 1 - distance / (halfWidth + 1));
                            // bildArray[ny][nx] += increment * factor;
                            // if (bildArray[ny][nx] > 1) bildArray[ny][nx] = 1;
                            // visited[ny][nx] = true;
                        // }
                    // }
                // }
            // }

            // x += xIncrement;
            // y += yIncrement;
        // }
    // }

    public double averageColour() {
        double summ = 0;
        double ans = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bildArray[y][x] != -1) {
                    summ = summ + bildArray[y][x];
                    ans++;
                }
            }
        }
        return summ / ans;
    }

    public void setDiameter(int pDiameter) {
        diameter = pDiameter;
    }
}
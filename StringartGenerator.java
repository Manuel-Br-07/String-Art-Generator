import java.util.function.Consumer;

/**
 * Beschreiben Sie hier die Klasse StringartGenerator.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class StringartGenerator
{
    private Data data;
    private Queue<int[]> lineOrder;
    private StringArtPlotter stringArtPlotter;

    //immageToArray
    private int width;
    private int height;
    private double[][] bildArray;

    //listToArray
    private int[][] lineOrderArray;

    //nailPositions
    private int nails;
    private double[][] nailCoords;

    //setScale
    private double diameter;
    private double mmProPixel;

    double lineWidth;
    double lineStrength;
    int maxIterations;

    // Listener für Fortschritt
    private Consumer<Double> progressListener;

    // Setter für den Listener
    public void setProgressListener(Consumer<Double> listener) {
        this.progressListener = listener;
    }

    /**
     * Konstruktor für Objekte der Klasse StringartGenerator
     */
    public StringartGenerator(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter)
    {
        data = pData;
        lineOrder = pLineOrder;
        stringArtPlotter = pStringArtPlotter;
    }

    public void main()
    {
        // ------------------ GET ------------------
        width = data.getWidth();
        height = data.getHeight();
        bildArray = generateArray(data.getBildArray());

        nails = data.getNails();
        nailCoords = data.getNailCoords();

        diameter = data.getDiameter();
        mmProPixel = data.getMmProPixel();

        lineWidth = data.getLineWidth();
        lineStrength = data.getLineStrength();
        maxIterations = data.getMaxIterations();
        // ------------------ BEARBEITUNG ------------------

        stringArtGenerator();

        // ------------------ SET ------------------
        // data.setlineOrderArray(lineOrderArray);
        data.setNails(nails);
        data.setDiameter(diameter);
        data.setMmProPixel(mmProPixel);
        data.setNailCoords(nailCoords);
        data.setLineWidth(lineWidth);
    }

    public int stringArtGenerator() {
        int startNail = 0;
        int iterations = 0;
        int[] linePos;
        double endpoint = averageColour() + (1 - averageColour()) / 10 * 8.5;
        System.out.println("Endpoint " + endpoint);

        for(int i=0; i < maxIterations; i++)
        {
            // while (averageColour() <= endpoint && iterations < 50000) {
            double bestScore = 0;
            int bestEndNail = -1;
            for (int endNail = 0; endNail < nails; endNail++) {
                if (endNail != startNail)// gleiche Nagel überspringen
                {
                    // Berechne Score für Linie von startNail zu endNail
                    double score = calculateLineScore(startNail, endNail);
                    // System.out.println("Scores " + score + " " + bestScore);
                    if (score > bestScore) {
                        bestScore = score;
                        bestEndNail = endNail;
                    }
                }
            }

            // System.out.println("xmin " + nailCoords[startNail][0] + " ymin " + nailCoords[startNail][1] + " xmax " + nailCoords[bestEndNail][0] + " ymax " + nailCoords[bestEndNail][1]);
            // System.out.println("Start " + startNail + " Ende " + bestEndNail);
            if (bestEndNail != -1) {
                StringArtPlotter.addLine(nailCoords[startNail][0], nailCoords[startNail][1], nailCoords[bestEndNail][0], nailCoords[bestEndNail][1]);
                // StringArtPlotter.show();

                linePos = new int[]{startNail, bestEndNail};
                lineOrder.enqueue(linePos);

                lightenLine(startNail, bestEndNail, lineStrength);
                startNail = bestEndNail;
                iterations++;

                // meldet Progress an die Progressbar
                if (progressListener != null) {
                    progressListener.accept((double) iterations / maxIterations);
                    // System.out.println("Wert weitergegeben");
                }
            }
        }

        System.out.println("iterations " + iterations + " average " + averageColour());
        listToArray();

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

    public void listToArray()
    {
        int length = 0;

        int[] lEndpoint = lineOrder.front();
        int[] lLineData = null;

        while(lLineData != lEndpoint)
        {
            lLineData = lineOrder.front();
            length ++;
            lineOrder.dequeue();
            lineOrder.enqueue(lLineData);
            lLineData = lineOrder.front();
        }

        lineOrderArray = new int[length][2];

        for(int i = 0; i < lineOrderArray.length; i++)
        {
            lineOrderArray[i][0] = lineOrder.front()[0];
            lineOrderArray[i][1] = lineOrder.front()[1];
            lineOrder.dequeue();
        }

    }

    public double averageColour()
    {
        double summ = 0;
        double ans = 0;

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                if (bildArray[y][x] != -1)
                {
                    summ = summ + bildArray[y][x];
                    ans++;
                }
            }
        }
        return summ / ans;
    }

    private double[][] generateArray(double[][] pArray)
    {
        int x = pArray.length;
        int y = pArray[0].length;
        double[][] array = new double[x][y];

        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < y; j++)
            {
                array[i][j] = pArray[i][j];
            }
        }

        return array;
    }
}

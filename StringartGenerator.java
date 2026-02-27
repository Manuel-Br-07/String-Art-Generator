
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

    //immageToArray
    private double[][][] bildArray;

    //nailPositions
    private double[][] nailCoords;
    private int[][] possibleNails;

    double[] lineStrength;
    int[] maxIterations;

    /**
     * Konstruktor für Objekte der Klasse StringartGenerator
     */
    public StringartGenerator(Data pData, Queue<int[]> pLineOrder)
    {
        data = pData;
        lineOrder = pLineOrder;
    }

    public void artGen(int colorChannel) {
        bildArray = duplicateArray(data.getBildArray());
        maxIterations = data.getMaxIterations();
        possibleNails = data.getPossibleNails();

        lineStrength = data.getLineStrength();

        int startNail = 0;
        int iterations = 0;
        int[] linePos;

        for(int i = 0; i < maxIterations[colorChannel]; i++)
        {
            double bestScore = 0;
            int bestEndNail = -1;

            for (int j = 0; j < possibleNails[startNail].length; j++) {
                int endNail = possibleNails[startNail][j];

                // Berechne Score für Linie von startNail zu endNail
                double score = calculateLineScore(startNail, endNail, colorChannel);
                // System.out.println("Scores " + score + " " + bestScore);
                if (score > bestScore) {
                    bestScore = score;
                    bestEndNail = endNail;
                }

            }

            // System.out.println("xmin " + nailCoords[startNail][0] + " ymin " + nailCoords[startNail][1] + " xmax " + nailCoords[bestEndNail][0] + " ymax " + nailCoords[bestEndNail][1]);
            System.out.println("i " + i + " Start " + startNail + " Ende " + bestEndNail);
            if (bestEndNail != -1) {

                linePos = new int[]{startNail, bestEndNail};
                lineOrder.enqueue(linePos);

                lightenLine(startNail, bestEndNail, lineStrength[colorChannel], colorChannel);
                startNail = bestEndNail;
                iterations++;

            }
        }

        // System.out.println("iterations " + iterations + " average " + averageColour());
        listToArray(colorChannel);
    }

    public double calculateLineScore(int startNail, int endNail, int colorChannel) {
        
        nailCoords = data.getNailCoords();
        
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
                score += 1.0 - bildArray[py][px][colorChannel];
                countedPoints++;
            }

            x += xIncrement;
            y += yIncrement;
        }

        return countedPoints > 0 ? score / countedPoints : 0.0;
    }

    public void lightenLine(int startNail, int endNail, double increment, int colorChannel)
    {
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
                bildArray[py][px][colorChannel] += increment;
                if (bildArray[py][px][colorChannel] > 1) {
                    bildArray[py][px][colorChannel] = 1;
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

    public void listToArray(int colorChannel)
    {
        int[][][] lineOrderArray = data.getLineOrderArray();
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

        lineOrderArray[colorChannel] = new int[length][2];

        for(int i = 0; i < lineOrderArray[colorChannel].length; i++)
        {
            lineOrderArray[colorChannel][i][0] = lineOrder.front()[0];
            lineOrderArray[colorChannel][i][1] = lineOrder.front()[1];
            lineOrder.dequeue();
        }
        data.setLineOrderArray(lineOrderArray[colorChannel], colorChannel);
    }

    // public double averageColour()
    // {
    // double summ = 0;
    // double ans = 0;

    // for (int y = 0; y < height; y++)
    // {
    // for (int x = 0; x < width; x++)
    // {
    // if (bildArray[y][x][colorMode] != -1)
    // {
    // summ = summ + bildArray[y][x];
    // ans++;
    // }
    // }
    // }
    // return summ / ans;
    // }

    private double[][][] duplicateArray(double[][][] pArray)
    {
        int x = pArray.length;
        int y = pArray[0].length;
        int c = pArray[0][0].length;
        double[][][] array = new double[x][y][c];
        
        for(int i = 0; i < x; i++)
        for(int j = 0; j < y; j++)
        {
            for(int k = 0; k < c; k++)
            {
                array[i][j][k] = pArray[i][j][k];
            }
        }

        return array;
    }
}

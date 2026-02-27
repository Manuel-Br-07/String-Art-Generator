import java.util.Queue;
import java.util.LinkedList;

public class Main {
    private Data data;

    private ImageToArray imageToArray;
    private StringartGenerator stringartGen;
    private GcodeGenerator gcodeGen;

    public Main(Data pData, ImageToArray pImageToArray, StringartGenerator pStringartGen, GcodeGenerator pGcodeGen)
    {
        data = pData;
        imageToArray = pImageToArray;
        stringartGen = pStringartGen;
        // gcodeGen = pGcodeGen;

    }

    // ---------------- Nägel ----------------
    public void nailPositions(int pNails) {
        int nails = pNails;

        double centerX = data.getCenterX();
        double centerY = data.getCenterY();
        double radius = data.getRadius();

        double[][] nailCoords;

        nailCoords = new double[nails][2];
        for (int i = 0; i < nails; i++) {
            double angle = 2 * Math.PI * i / nails;
            nailCoords[i][0] = centerX + radius * Math.cos(angle);
            nailCoords[i][1] = centerY + radius * Math.sin(angle);
        }

        data.setNailCoords(nailCoords);
        data.setNails(nails);
        avoidColisions();
    }

    public void avoidColisions()
    {
        Queue<Integer> temporary = new LinkedList<>();
        double nailWidth = data.getNailWidth();
        double pinWidth = data.getPinWidth();
        double[][] nailCoords = data.getNailCoords();
        int[][] possibleNails = new int[nailCoords.length][];

        for(int i = 0; i < nailCoords.length; i++)
        {
            for(int j = 0; j < nailCoords.length; j++)
            {

                // int prev = (i - 1 + nailCoords.length) % nailCoords.length;
                // int next = (i + 1) % nailCoords.length;
                // if(i != j && i != prev && i != next)
                int diff = Math.abs(i - j);
                int dist = Math.min(diff, nailCoords.length - diff);

                if (i != j && dist > 2) 
                {
                    double startNailX = nailCoords[i][0];
                    double startNailY = nailCoords[i][1];
                    double endNailX = nailCoords[j][0];
                    double endNailY = nailCoords[j][1];
                    boolean colision = false;
                    for(int k = 0; k < nailCoords.length; k++)
                    {
                        double checkNailX = nailCoords[k][0];
                        double checkNailY = nailCoords[k][1];

                        double t = ((checkNailX - startNailX) * (endNailX - startNailX) + (checkNailY - startNailY) * (endNailY - startNailY)) /
                            (Math.pow(endNailX - startNailX, 2) + Math.pow(endNailY - startNailY, 2));

                        if(t <= 0 || t >= 1 )
                        {
                            continue;
                        }

                        double projX = startNailX + t * (endNailX - startNailX);
                        double projY = startNailY + t * (endNailY - startNailY);

                        double distance = Math.sqrt(Math.pow(checkNailX - projX, 2) + Math.pow(checkNailY - projY, 2));

                        if(distance < (nailWidth + pinWidth) / 2)
                        {
                            colision = true;
                            break;
                        }

                    }
                    if(!colision)
                        temporary.offer(j);

                }
            }

            possibleNails[i] = new int[temporary.size()];
            int j = 0;
            while(!temporary.isEmpty())
            {
                possibleNails[i][j] = temporary.poll();
                j++;
            }

        }
        data.setPossibleNails(possibleNails);
        print();
    }

    public void print()
    {
        int[][] possibleNails = data.getPossibleNails();
        for(int i = 0; i < possibleNails.length; i++)
        {
            for(int j = 0; j < possibleNails[i].length; j++)
            {
                System.out.print(possibleNails[i][j] + "   ");

            }
            System.out.println();
        }
    }

    // public double calculateStringLength()
    // {
    // double length = 0;
    // double[][] nailCoords = data.getNailCoords();
    // double mmProPixel = data.getMmProPixel();
    // int[][] lineOrderArray = data.getlineOrderArray();
    // int currentIteration = data.getCurrentIteration();

    // if(lineOrderArray != null)
    // for(int i = 0; i < lineOrderArray.length && i < currentIteration; i++)
    // {
    // length = length + Math.sqrt(Math.pow(nailCoords[lineOrderArray[i][01]][0] - nailCoords[lineOrderArray[i][0]][0], 2) + Math.pow(nailCoords[lineOrderArray[i][1]][1] - nailCoords[lineOrderArray[i][1]][1], 2)) * mmProPixel;
    // }

    // data.setStringLength(length);
    // return length;
    // }

    public void setScale(double pDiameter) {
        data.setDiameter(pDiameter);
        data.setMmProPixel(pDiameter / data.getWidth());
    }
}
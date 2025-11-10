
public class Main {
    private Data data = new Data();
    private Queue<int[]> lineOrder = new Queue();
    private StringArtPlotter stringArtPlotter = new StringArtPlotter();

    private ImageToArray imageToArray = new ImageToArray(data, lineOrder, stringArtPlotter);
    private StringartGenerator stringartGen = new StringartGenerator(data, lineOrder, stringArtPlotter);
    private GcodeGenerator gcodeGen = new GcodeGenerator(data, lineOrder, stringArtPlotter);

    // //immageToArray
    // private int width;
    // private int height;
    // private double[][] bildArray;
    // private String  dateiName;

    // //circularMask
    // private double centerX;
    // private double centerY;
    // private double radius;

    // //nailPositions
    // private int nails;
    // private double[][] nailCoords;  

    // private double diameter;
    // private double mmProPixel;

    // double lineWidth;
    public Main()
    {
        // imageToArray("SmileyRGB.png");
        // nailPositions(150);
        // stringartGenerator();
        // setScale(250);
        // calculateStringLength();
    }

    public void imageToArray(String  dateiName)
    {
        data.setDateiName(dateiName);
        imageToArray.main();
    }

    public void stringartGenerator()
    {
        stringartGen.main();
    }

    public void gcodeGenerator()
    {
        gcodeGen.main();
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

        data.setNailCoords(nailCoords);
        data.setNails(nails);
    }

    public void calculateStringLength()
    {
        double length = 0;
        int nails = data.getNails();
        double[][] nailCoords = data.getNailCoords();
        double mmProPixel = data.getMmProPixel();
        
        int[] lEndpoint = lineOrder.front();
        int[] lLineData = null;

        while(lLineData != lEndpoint)
        {
            lLineData = lineOrder.front();
            length = length + Math.sqrt(Math.pow(nailCoords[lLineData[1]][0] - nailCoords[lLineData[0]][0], 2) + Math.pow(nailCoords[lLineData[1]][1] - nailCoords[lLineData[0]][1], 2)) * mmProPixel;
            lineOrder.dequeue();
            lineOrder.enqueue(lLineData);
            lLineData = lineOrder.front();
        }
        
        System.out.println("length " + length);
        data.setStringLength(length);
    }

    public void setScale(int pDiameter) {
        data.setDiameter(pDiameter);
        data.setMmProPixel((double) pDiameter / data.getWidth());
        calculateStringLength();
    }
}
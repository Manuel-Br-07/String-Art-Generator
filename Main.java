
public class Main {
    private Data data;
    private Queue<int[]> lineOrder;
    private StringArtPlotter stringArtPlotter;

    private ImageToArray imageToArray;
    private StringartGenerator stringartGen;
    private GcodeGenerator gcodeGen;

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
    public Main(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter, ImageToArray pImageToArray, StringartGenerator pStringartGen, GcodeGenerator pGcodeGen)
    {
        data = pData;
        lineOrder = pLineOrder;
        stringArtPlotter = pStringArtPlotter;
        imageToArray = pImageToArray;
        stringartGen = pStringartGen;
        gcodeGen = pGcodeGen;
        
        // imageToArray("SmileyRGB.png");
        // imageToArray("TestPortraitRGB.png");
        // imageToArray("IMG_1443.png");
        // imageToArray();
        // nailPositions(300);
        // stringartGenerator();
        // setScale(250);
        // calculateStringLength();
        // gcodeGenerator();
    }

    public void imageToArray()//String  dateiName)
    {
        // data.setDateiName(dateiName);
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

    public double calculateStringLength()
    {
        double length = 0;
        double[][] nailCoords = data.getNailCoords();
        double mmProPixel = data.getMmProPixel();
        int[][] lineOrderArray = data.getlineOrderArray();
        int currentIteration = data.getCurrentIteration();

        if(lineOrderArray != null)
        for(int i = 0; i < lineOrderArray.length && i < currentIteration; i++)
        {
            length = length + Math.sqrt(Math.pow(nailCoords[lineOrderArray[i][01]][0] - nailCoords[lineOrderArray[i][0]][0], 2) + Math.pow(nailCoords[lineOrderArray[i][1]][1] - nailCoords[lineOrderArray[i][1]][1], 2)) * mmProPixel;
        }

        data.setStringLength(length);
        return length;
    }

    public void setScale(int pDiameter) {
        data.setDiameter(pDiameter);
        data.setMmProPixel((double) pDiameter / data.getWidth());
    }
    
    public double setAbstand()
    {
        data.setNailDistance((Math.PI * data.getDiameter()) / data.getNails());
        return (Math.PI * data.getDiameter()) / data.getNails();
    }
}
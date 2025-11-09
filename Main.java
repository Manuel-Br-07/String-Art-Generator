

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

    double lineWidth = 2;
    
    
    private Data data = new Data();
    private Queue<int[]> lineOrder = new Queue();
    private StringArtPlotter stringArtPlotter = new StringArtPlotter();
    
    private ImageToArray imageToArray = new ImageToArray(data, lineOrder, stringArtPlotter);
    private StringartGenerator stringartGen = new StringartGenerator(data, lineOrder, stringArtPlotter);
    private GcodeGenerator GcodeGen = new GcodeGenerator(data, lineOrder, stringArtPlotter);
    

    public Main()
    {
        
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
        // gcodeGen.main();
    }

    // ---------------- Nägel ----------------
    public void nailPositions(int pNails) {
        nails = pNails;
        
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
    }

    

    public void setDiameter(int pDiameter) {
        diameter = pDiameter;
    }
}
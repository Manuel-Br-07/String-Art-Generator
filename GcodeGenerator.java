
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Beschreiben Sie hier die Klasse GcodeGenerator.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class GcodeGenerator
{
    private Data data;
    private Queue<int[]> lineOrder;
    private StringArtPlotter stringArtPlotter;
    private BufferedWriter writer;

    //immageToArray
    // private int width;
    // private int height;
    // private double[][] bildArray;
    private String  dateiName;

    //circularMask
    private double centerX;
    private double centerY;
    private double radius;

    //listToArray
    private int[][] lineOrderArray;

    //nailPositions
    private int nails;
    private double[][] nailCoords;

    private double diameter;
    private double mmProPixel;
    private double stringLength;

    //calculateCoordinates
    double[][] absoluteNailPositions;
    double gapsize;

    /**
     * Konstruktor für Objekte der Klasse GcodeGenerator
     */
    public GcodeGenerator(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter)
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

        nails = data.getNails();
        nailCoords = data.getNailCoords();

        lineOrderArray = data.getlineOrderArray();

        diameter = data.getDiameter();
        mmProPixel = data.getMmProPixel();
        stringLength = data.getStringLength();

        // lineWidth = data.getLineWidth();

        absoluteNailPositions = data.getAbsoluteNailPositions();
        gapsize = data.getGapsize();

        // ------------------ BEARBEITUNG ------------------

        GCodeControll();

        // ------------------ SET ------------------
        // data.setBildArray(bildArray); // falls bearbeitet
        // data.setNails(nails);
        // data.setDiameter(diameter);
        // data.setMmProPixel(mmProPixel);
        // data.setNailCoords(nailCoords);
        // data.setLineWidth(lineWidth);
        data.setCenterX(centerX);
        data.setCenterY(centerY);
        data.setRadius(radius);
        data.setAbsoluteNailPositions(absoluteNailPositions);
        data.setGapsize(gapsize);
    }

    public void GCodeControll()
    {
        calculateCoordinates();
        fileGen();

        for(int i = 1; i < absoluteNailPositions.length; i++)
        {
            double NailX = absoluteNailPositions[i][0];
            double NailY = absoluteNailPositions[i][1];
            double BorderX = absoluteNailPositions[i][2];
            double BorderY = absoluteNailPositions[i][3];

        }

        schliessen();
    }

    public void calculateCoordinates()
    {
        absoluteNailPositions = new double[lineOrderArray.length][4];
        
        for(int i = 0; i < absoluteNailPositions.length; i++)
        {
            double dx = nailCoords[lineOrderArray[i][1]][0] - centerX;
            double dy = nailCoords[lineOrderArray[i][1]][1] - centerY;
            double length = Math.sqrt(dx * dx + dy * dy);
            double factor = (length - gapsize) / length;

            absoluteNailPositions[i][0] = nailCoords[lineOrderArray[i][1]][0];
            absoluteNailPositions[i][1] = nailCoords[lineOrderArray[i][1]][1];
            absoluteNailPositions[i][2] = centerX + dx * factor;
            absoluteNailPositions[i][3] = centerY + dy * factor;
            System.out.println("x " + absoluteNailPositions[i][0] + " y " + absoluteNailPositions[i][1] + " x1 " + absoluteNailPositions[i][2] + " y1 " + absoluteNailPositions[i][3]);
        }

    }

    public void fileGen()
    {
        String string = ("StringArtGcode.txt");
        try {
            writer = new BufferedWriter(new FileWriter(string, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine(String pString) {
        try {
            writer.write(pString);
            writer.newLine();  // Zeilenumbruch
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void schliessen() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

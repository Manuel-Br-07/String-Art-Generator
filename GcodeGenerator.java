
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

    //printerControlls
    private String gCodeName;
    private int zHop;
    private int speedCircle;
    private int speedTravel;
    private int acceleration;
    private int radiusNails;
    private double distanceX;
    private double distanceY;

    /**
     * Konstruktor für Objekte der Klasse GcodeGenerator
     */
    public GcodeGenerator(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter)
    {
        data = pData;
        lineOrder = pLineOrder;
        stringArtPlotter = pStringArtPlotter;
    }

    public void main(String filename)
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

        //printerControlls
        gCodeName  = data.getGCodeFile();
        zHop = data.getZHop();
        speedCircle = data.getSpeedCircle();
        speedTravel = data.getSpeedTravel();
        acceleration = data.getAcceleration();
        distanceX = data.getDistanceX();
        distanceY = data.getDistanceY();

        // ------------------ BEARBEITUNG ------------------
        GCodeControll(filename);
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

    public void GCodeControll(String filename)
    {
        calculateCoordinates();
        double nailX = absoluteNailPositions[0][0];
        double nailY = absoluteNailPositions[0][1];
        double borderX = absoluteNailPositions[0][2];
        double borderY = absoluteNailPositions[0][3];


        fileGen(filename);

        //G-Code initialize:

        writeLine("SET_VELOCITY_LIMIT ACCEL=" + acceleration);
        writeLine("G92 X" + nailX + " Y" + nailY + " Z0");
        writeLine("G1 X" + nailX / 2 + " Y" + nailY + " F" + speedCircle / 2);
        writeLine("Base_Pause");
        
        // writeLine("G1 X" + borderX + "Y " + borderY + " F" + speedTravel);
        // // writeLine(calculateG2(nailX, nailY, borderX, borderY, true));
        // writeLine(calculateG2(nailX, nailY, borderX, borderY, false));

        for(int i = 0; i < absoluteNailPositions.length; i++)
        {
            nailX = absoluteNailPositions[i][0];
            nailY = absoluteNailPositions[i][1];
            borderX = absoluteNailPositions[i][2];
            borderY = absoluteNailPositions[i][3];
            
            if(i % 100 == 0)
            {
                double z =  (zHop / (absoluteNailPositions.length / 100.0)) * (i / 100.0);
                writeLine("G1 Z" + z);
            }

            //G-Code write:
            
            writeLine("G1 X" + borderX + " Y" + borderY + " F" + (speedTravel));
            writeLine(calculateG2(nailX, nailY, borderX, borderY, false));
        }
        writeLine("");

        schliessen();
    }

    public void calculateCoordinates()
    {
        absoluteNailPositions = new double[data.getCurrentIteration() + nails][4];

        for(int i = 0; i < nails; i++)
        {
            double dx = nailCoords[i][0] - centerX;
            double dy = nailCoords[i][1] - centerY;
            double length = Math.sqrt(dx * dx + dy * dy);
            double factor = (length - gapsize) / length;

            absoluteNailPositions[i][0] = ((nailCoords[i][0]) * mmProPixel) + distanceX;
            absoluteNailPositions[i][1] = ((nailCoords[i][1]) * mmProPixel) + distanceY;
            absoluteNailPositions[i][2] = ((centerX + dx * factor) * mmProPixel) + distanceX;
            absoluteNailPositions[i][3] = ((centerY + dy * factor) * mmProPixel) + distanceY;
        }
        
        for(int i = nails; i < absoluteNailPositions.length; i++)
        {
            double dx = nailCoords[lineOrderArray[i][1]][0] - centerX;
            double dy = nailCoords[lineOrderArray[i][1]][1] - centerY;
            double length = Math.sqrt(dx * dx + dy * dy);
            double factor = (length - gapsize) / length;

            absoluteNailPositions[i][0] = ((nailCoords[lineOrderArray[i][1]][0]) * mmProPixel) + distanceX;
            absoluteNailPositions[i][1] = ((nailCoords[lineOrderArray[i][1]][1]) * mmProPixel) + distanceY;
            absoluteNailPositions[i][2] = ((centerX + dx * factor) * mmProPixel) + distanceX;
            absoluteNailPositions[i][3] = ((centerY + dy * factor) * mmProPixel) + distanceY;
            // System.out.println("x " + absoluteNailPositions[i][0] + " y " + absoluteNailPositions[i][1] + " x1 " + absoluteNailPositions[i][2] + " y1 " + absoluteNailPositions[i][3]);
        }

    }

    public String calculateG2(double nailX, double nailY, double borderX, double borderY, boolean half)
    {
        double i = nailX - borderX;
        double j = nailY - borderY;
        double x = 2 * nailX - borderX;
        double y = 2 * nailY - borderY;
        if(half == true)
        {
            return "G2 X" + x + " Y" + y + " I" + i + " J" + j + " F" + (speedCircle);
        }
        return "G2 X" + borderX + " Y" + borderY + " I" + i + " J" + j + " F" + (speedCircle);

    }

    public void fileGen(String string)
    {
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

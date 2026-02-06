
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
    private double[][] compensatedNailCoords;
    private double[][] nailCoords;

    private double diameter;
    private double mmProPixel;
    private double stringLength;

    //calculateCoordinates
    double[][] absoluteNailPositions;
    double gapsize;
    
    //GuiControllerSettings 
    private double nailDistance;

    //printerControlls
    private String gCodeName;
    private int zHop;
    private int speedCircle;
    private int speedTravel;
    private int acceleration;
    private int radiusNails;

    private double heightStartingnail;

    private double coordinateXRight;
    private double coordinateYRight;
    private double coordinateXLeft;
    private double coordinateYLeft;

    private double compensationAngle;
    private double absoluteDistance;
    
    private double zHeight;

    /**
     * Konstruktor für Objekte der Klasse GcodeGenerator
     */
    public GcodeGenerator(Data pData, Queue<int[]> pLineOrder)
    {
        data = pData;
        lineOrder = pLineOrder;
    }

    public void main(String filename)
    {
        // ------------------ GET ------------------
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

        absoluteNailPositions = data.getAbsoluteNailPositions();
        gapsize = data.getGapsize();
        
        nailDistance = data.getNailDistance();

        //printerControlls
        gCodeName  = data.getGCodeFile();
        zHop = data.getZHop();
        speedCircle = data.getSpeedCircle();
        speedTravel = data.getSpeedTravel();
        acceleration = data.getAcceleration();
        heightStartingnail =data.getHeightStartingnail();
        coordinateXRight = data.getCoordinateXRight();
        coordinateYRight = data.getCoordinateYRight();
        coordinateXLeft = data.getCoordinateXLeft();
        coordinateYLeft = data.getCoordinateYLeft();

        compensationAngle = data.getCompensationAngle();
        absoluteDistance = data.getAbsoluteDistance();

        // ------------------ BEARBEITUNG ------------------
        GCodeControll(filename);
        // ------------------ SET ------------------
        data.setCenterX(centerX);
        data.setCenterY(centerY);
        data.setRadius(radius);
        data.setAbsoluteNailPositions(absoluteNailPositions);
        data.setGapsize(gapsize);
        data.setCompensationAngle(compensationAngle);
        data.setAbsoluteDistance(absoluteDistance);
    }

    public void GCodeControll(String filename)
    {
        calculateCompensationAngles();
        calculateCoordinates();
        double nailX = absoluteNailPositions[0][0];
        double nailY = absoluteNailPositions[0][1];
        double borderX = absoluteNailPositions[0][2];
        double borderY = absoluteNailPositions[0][3];

        fileGen(filename);
        //G-Code initialize:
        writeLine("SET_VELOCITY_LIMIT ACCEL=" + acceleration);
        writeLine("G92 X" + toDecimal(nailX) + " Y" + toDecimal(nailY) + " Z" + heightStartingnail);
        writeLine("G1 X" + toDecimal(borderX - 15) + " Y" + toDecimal(nailY) + " F" + speedCircle / 2);
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

            if(i % 10 == 0)
            {
                zHeight =  (zHop / (absoluteNailPositions.length / 10.0)) * (i / 10.0);
                writeLine("G1 Z" + toDecimal(zHeight));
            }

            //G-Code write:

            writeLine("G1 Z" + toDecimal(zHeight + 1) + " F" + (speedTravel));
            writeLine("G1 X" + toDecimal(borderX) + " Y" + toDecimal(borderY) + " F" + (speedTravel));
            writeLine("G1 Z" + toDecimal(zHeight) + " F" + (speedTravel));
            writeLine(calculateG2(nailX, nailY, borderX, borderY, true)); 
            writeLine(calculateG2(nailX, nailY, borderX, borderY, false));  
        }

        writeLine("G1 X" + (centerX * mmProPixel) + " Y" + (centerY * mmProPixel) + " Z" + (zHop + (heightStartingnail * 2)) + " F" + (speedTravel / 2));
        writeLine("");

        schliessen();
    }

    public void calculateCoordinates()
    {
        absoluteNailPositions = new double[data.getCurrentIteration() + nails][4];
        double distance = nailDistance / 2;

        for(int i = 0; i < nails; i++)
        {
            double dx = compensatedNailCoords[i][0] - centerX;
            double dy = compensatedNailCoords[i][1] - centerY;
            double length = Math.sqrt(dx * dx + dy * dy);
            double factor = (length - (distance / mmProPixel)) / length;

            absoluteNailPositions[i][0] = ((compensatedNailCoords[i][0]) * mmProPixel) + distance;
            absoluteNailPositions[i][1] = ((compensatedNailCoords[i][1]) * mmProPixel) + distance;
            absoluteNailPositions[i][2] = ((centerX + dx * factor) * mmProPixel) + distance;
            absoluteNailPositions[i][3] = ((centerY + dy * factor) * mmProPixel) + distance;
        }
        
        for(int i = 0; i < lineOrderArray.length; i++)
        {
            double dx = compensatedNailCoords[lineOrderArray[i][1]][0] - centerX;  
            double dy = compensatedNailCoords[lineOrderArray[i][1]][1] - centerY;
            double length = Math.sqrt(dx * dx + dy * dy);
            double factor = (length - (distance / mmProPixel)) / length;

            absoluteNailPositions[i + nails][0] = ((compensatedNailCoords[lineOrderArray[i][1]][0]) * mmProPixel) + distance;
            absoluteNailPositions[i + nails][1] = ((compensatedNailCoords[lineOrderArray[i][1]][1]) * mmProPixel) + distance;
            absoluteNailPositions[i + nails][2] = ((centerX + dx * factor) * mmProPixel) + distance;
            absoluteNailPositions[i + nails][3] = ((centerY + dy * factor) * mmProPixel) + distance;
            // System.out.println("x " + absoluteNailPositions[i][0] + " y " + absoluteNailPositions[i][1] + " x1 " + absoluteNailPositions[i][2] + " y1 " + absoluteNailPositions[i][3]);
        }

    }

    public String calculateG2(double nailX, double nailY, double borderX, double borderY, boolean half)
    {
        double i = nailX - borderX;
        double j = nailY - borderY;
        double x = 2 * nailX - borderX;
        double y = 2 * nailY - borderY;

        if(half)
        {
            return "G2 X" + toDecimal(x) + " Y" + toDecimal(y) + " I" + toDecimal(i) + " J" + toDecimal(j) + " F" + (speedCircle);
        }

        i = nailX - x;
        j = nailY - y;

        return "G2 X" + toDecimal(borderX) + " Y" + toDecimal(borderY) + " I" + toDecimal(i) + " J" + toDecimal(j) + " F" + (speedCircle);

    }

    public void calculateCompensationAngles()
    {
        // compensationAngle
        // absoluteDistance
        int opposite = (int)Math.ceil(nails / 2.0);

        double Ax1 = coordinateXRight;
        double Ay1 = coordinateYRight;
        double Ax2 = coordinateXLeft;
        double Ay2 = coordinateYLeft;

        double Rx1 = nailCoords[0][0];
        double Ry1 = nailCoords[0][1];
        double Rx2 = nailCoords[opposite][0];
        double Ry2 = nailCoords[opposite][1];

        double angleR = Math.atan((Ry2 - Ry1) / (Rx2 - Rx1));
        double angleA = Math.atan((Ay2 - Ay1) / (Ax2 - Ax1));
        compensationAngle = angleA - angleR;
        absoluteDistance = Math.hypot(Ax2 - Ax1, Ay2 - Ay1);

        compensatedNailCoords = new double[nailCoords.length][2];
        for(int i = 0; i < nails; i++)
        {
            double x = nailCoords[i][0] - centerX;
            double y = nailCoords[i][1] - centerY;
            double h = Math.sqrt(Math.pow((x) , 2) + Math.pow((y) , 2));
            double a = Math.atan2(y, x) + compensationAngle;

            compensatedNailCoords[i][0] = (h * Math.cos(a)) + centerX;
            compensatedNailCoords[i][1] = (h * Math.sin(a)) + centerY;

        }
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

    public String toDecimal(double value)
    {
        DecimalFormat df = new DecimalFormat("0.##########",
                DecimalFormatSymbols.getInstance(Locale.US));

        return df.format(value);
    }
}

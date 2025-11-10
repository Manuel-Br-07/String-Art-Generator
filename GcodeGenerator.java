
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

    //nailPositions
    private int nails;
    private double[][] nailCoords;

    private double diameter;
    private double mmProPixel;
    private double stringLength;

    // double lineWidth;
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

        nails = data.getNails();
        nailCoords = data.getNailCoords();

        diameter = data.getDiameter();
        mmProPixel = data.getMmProPixel();
        stringLength = data.getStringLength();

        // lineWidth = data.getLineWidth();

        // ------------------ BEARBEITUNG ------------------

        GcodeFileGen();
        schreibeZeile();
        schliessen();

        // ------------------ SET ------------------
        // data.setBildArray(bildArray); // falls bearbeitet
        // data.setNails(nails);
        // data.setDiameter(diameter);
        // data.setMmProPixel(mmProPixel);
        // data.setNailCoords(nailCoords);
        // data.setLineWidth(lineWidth);
    }

    public void GcodeFileGen()
    {
        String string = ("StringArtGcode.txt");
        try {
            writer = new BufferedWriter(new FileWriter(string, false));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void schreibeZeile() {
        try {
            writer.write("Test");
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

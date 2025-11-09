
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
    
    private int width = 0;
    private int height = 0;
    private double[][] bildArray;

    private double centerX;
    private double centerY;
    private double radius;

    private int nails = 0;
    private double diameter = 0;
    private double mmProPixel = 0;
    private double[][] nailCoords;

    double lineWidth = 2;

    /**
     * Konstruktor für Objekte der Klasse GcodeGenerator
     */
    public GcodeGenerator(Data pData, Queue<int[]> pLineOrder, StringArtPlotter pStringArtPlotter)
    {
        data = pData;
        lineOrder = pLineOrder;
        stringArtPlotter = pStringArtPlotter;
    }
}

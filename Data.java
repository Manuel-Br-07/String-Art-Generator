
/**
 * Beschreiben Sie hier die Klasse Data.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class Data
{
    // Instanzvariablen
    
    //immageToArray
    private int width = 0;
    private int height = 0;
    private double[][] bildArray;
    private String  dateiName;

    private double centerX;
    private double centerY;
    private double radius;

    private int nails = 0;
    private double diameter = 0;
    private double mmProPixel = 0;
    private double[][] nailCoords;

    double lineWidth = 2;

    /**
     * Konstruktor für Objekte der Klasse Data
     */
    public Data()
    {
        
    }
    
    // get- / set-Methoden:
    
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double[][] getBildArray() {
        return bildArray;
    }

    public void setBildArray(double[][] bildArray) {
        this.bildArray = bildArray;
    }
    
    public String getDateiName() {
        return dateiName;
    }

    public void setDateiName(String dateiName) {
        this.dateiName = dateiName;
    }

    public double getCenterX() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX = centerX;
    }

    public double getCenterY() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY = centerY;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public int getNails() {
        return nails;
    }

    public void setNails(int nails) {
        this.nails = nails;
    }

    public double getDiameter() {
        return diameter;
    }

    public void setDiameter(double diameter) {
        this.diameter = diameter;
    }

    public double getMmProPixel() {
        return mmProPixel;
    }

    public void setMmProPixel(double mmProPixel) {
        this.mmProPixel = mmProPixel;
    }

    public double[][] getNailCoords() {
        return nailCoords;
    }

    public void setNailCoords(double[][] nailCoords) {
        this.nailCoords = nailCoords;
    }

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }
}

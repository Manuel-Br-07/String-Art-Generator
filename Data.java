import javafx.scene.paint.Color;
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
    private int width;
    private int height;
    private double[][] bildArray;
    private String dateiName;
    private String ordnerName;

    //circularMask
    private double centerX;
    private double centerY;
    private double radius;

    //listToArray
    private int[][] lineOrderArray;

    //nailPositions
    private int nails = 0;
    private double[][] nailCoords;

    //setScale
    private double diameter;
    private double mmProPixel;

    //calculateStringLength
    private double stringLength;

    //StringArtGenerator
    private double lineWidth = 2;

    //calculateCoordinates
    private double[][] absoluteNailPositions;
    private double gapsize = 3;

    //GuiControllerSettings  
    private int maxIterations;
    private int currentIteration;
    private double lineStrength = 0.6;
    private double lineWidthDisplay;
    private double nailDistance;
    private Color backgroundColor;
    private Color lineColor;

    //rückgabewerte
    private String textOutputStrings;

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

    public String getOrdnerName() {
        return ordnerName;
    }

    public void setOrdnerName(String ordnerName) {
        this.ordnerName = ordnerName;
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

    public int[][] getlineOrderArray() {
        return lineOrderArray;
    }

    public void setlineOrderArray(int[][] lineOrderArray) {
        this.lineOrderArray = lineOrderArray;
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

    public double getStringLength() {
        return stringLength;
    }

    public void setStringLength(double stringLength) {
        this.stringLength = stringLength;
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

    public double[][] getAbsoluteNailPositions() {
        return absoluteNailPositions;
    }

    public void setAbsoluteNailPositions(double[][] absoluteNailPositions) {
        this.absoluteNailPositions = absoluteNailPositions;
    }

    public double getGapsize() {
        return gapsize;
    }

    public void setGapsize(double gapsize) {
        this.gapsize = gapsize;
    }

    public int getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations) {
        this.maxIterations = maxIterations;
    }

    public int getCurrentIteration() {
        return currentIteration;
    }

    public void setCurrentIteration(int currentIteration) {
        this.currentIteration = currentIteration;
    }

    public double getLineStrength() {
        return lineStrength;
    }

    public void setLineStrength(double lineStrength) {
        this.lineStrength = lineStrength;
    }

    public double getLineWidthDisplay() {
        return lineWidthDisplay;
    }

    public void setLineWidthDisplay(double lineWidthDisplay) {
        this.lineWidthDisplay = lineWidthDisplay;
    }

    public double getNailDistance() {
        return nailDistance;
    }

    public void setNailDistance(double nailDistance) {
        this.nailDistance = nailDistance;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public String getTextOutputStrings() {
        return textOutputStrings;
    }

    public void setTextOutputStrings(String textOutputStrings) {
        this.textOutputStrings = textOutputStrings;
    }
}
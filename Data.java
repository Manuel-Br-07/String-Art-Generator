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
    private double[][][] bildArray; // [X][Y][farbkanal]
    private String dateiName;
    private String ordnerName;

    //circularMask
    private double centerX;
    private double centerY;
    private double radius;

    // imageProcessing
    private boolean colorsInverted;
    private double clippingMinValue = 0;
    private double clippingMaxValue = 1;
    private int colorMode = 0;  // 0. Schwarz, 1. Schwarzweiß, 2. CMYK
    private int colorChannel = 0; // 0. Schwarz, 1. Weiß, 2. Cyan, 3. Magenta, 4. Gelb
    private int[][] colorMapping = {{0}, {0, 1}, {0, 2, 3, 4}}; // mapping der Farbkanäle zu modus

    //listToArray
    private int[][][] lineOrderArray = new int[5][][]; // [Farbkanal][][0] -> Startnagel      [Farbkanal][][1] -> Endnagel

    //nailPositions
    private int nails = 60;
    private double nailDistance;
    private double[][] nailCoords; // [][0] -> X      [][1] -> Y
    private int[][] possibleNails;
    private double nailWidth = 1.9;
    private double pinWidth = 2.0;

    //setScale
    private double diameter = 100;
    private double mmProPixel;

    //calculateStringLength
    private double stringLength;

    //StringArtGenerator
    private double[] lineWidth = {481, 481, 481, 481,481};

    //calculateCoordinates
    private double[][][] absoluteNailPositions = new double[5][][];
    private double gapsize = 2.6;

    //GuiControllerSettings  
    private int[] maxIterations = {481, 481, 481, 481,481};
    private int[] currentIteration = {481, 481, 481, 481, 481};
    private double[] lineStrength = {0.796, 0.796, 0.796, 0.796, 0.796};
    private double[] lineWidthDisplay = {0.448, 0.448, 0.448, 0.448, 0.448};
    private double distanceToNail;
    private Color backgroundColor;
    private Color[] lineColor = {Color.BLACK, Color.WHITE, Color.CYAN, Color.MAGENTA, Color.YELLOW};

    //printerControlls
    private String gCodeFile;
    private int zHop = 5;
    private int speedCircle = 20;
    private int speedTravel = 40;
    private int acceleration = 1000;
    private double heightStartingnail = 7;
    private double coordinateXRight = 0;
    private double coordinateYRight = 0;
    private double coordinateXLeft = 0;
    private double coordinateYLeft = 0;

    private double compensationAngle;
    private double absoluteDistance;

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

    public double[][][] getBildArray() {
        return bildArray;
    }

    public void setBildArray(double[][][] bildArray) {
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

    public boolean getColorsInverted() {
        return colorsInverted;
    }

    public void setColorsInverted(boolean colorsInverted) {
        this.colorsInverted = colorsInverted;
    }

    public double getClippingMinValue() {
        return clippingMinValue;
    }

    public void setClippingMinValue(double clippingMinValue) {
        this.clippingMinValue = clippingMinValue;
    }

    public double getClippingMaxValue() {
        return clippingMaxValue;
    }

    public void setClippingMaxValue(double clippingMaxValue) {
        this.clippingMaxValue = clippingMaxValue;
    }
    
    public int getColorMode() {
        return colorMode;
    }

    public void setColorMode(int colorMode) {
        this.colorMode = colorMode;
    }
    
    public int getColorChannel() {
        return colorChannel;
    }

    public void setColorChannel(int colorChannel) {
        this.colorChannel = colorChannel;
    }
    
    public int[][] getColorMapping() {
        return colorMapping;
    }

    // public void setColorChannel(int[][] colorMapping) {
        // this.colorMapping = colorMapping;
    // }

    public int[][][] getLineOrderArray() {
        return lineOrderArray;
    }

    public void setLineOrderArray(int[][] lineOrderArray, int channel) {
        this.lineOrderArray[channel] = lineOrderArray;
    }

    public int getNails() {
        return nails;
    }

    public void setNails(int nails) {
        this.nails = nails;
    }
    
    public double getNailDistance() {
        return nailDistance;
    }

    public void setNailDistance(double nailDistance) {
        this.nailDistance = nailDistance;
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

    public int[][] getPossibleNails() {
        return possibleNails;
    }

    public void setPossibleNails(int[][] possibleNails) {
        this.possibleNails = possibleNails;
    }

    public double getNailWidth() {
        return nailWidth;
    }

    public void setNailWidth(double nailWidth) {
        this.nailWidth = nailWidth;
    }
    
    public double getPinWidth() {
        return pinWidth;
    }

    public void setPinWidth(double pinWidth) {
        this.pinWidth = pinWidth;
    }

    public double[] getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth, int channel) {
        this.lineWidth[channel] = lineWidth;
    }

    public double[][][] getAbsoluteNailPositions() {
        return absoluteNailPositions;
    }

    public void setAbsoluteNailPositions(double[][][] absoluteNailPositions) {
        this.absoluteNailPositions = absoluteNailPositions;
    }

    public double getGapsize() {
        return gapsize;
    }

    public void setGapsize(double gapsize) {
        this.gapsize = gapsize;
    }

    public int[] getMaxIterations() {
        return maxIterations;
    }

    public void setMaxIterations(int maxIterations, int channel) {
        this.maxIterations[channel] = maxIterations;
    }

    public int[] getCurrentIteration() {
        return currentIteration;
    }

    public void setCurrentIteration(int currentIteration, int channel) {
        this.currentIteration[channel] = currentIteration;
    }

    public double[] getLineStrength() {
        return lineStrength;
    }

    public void setLineStrength(double lineStrength, int channel) {
        this.lineStrength[channel] = lineStrength;
    }

    public double[] getLineWidthDisplay() {
        return lineWidthDisplay;
    }

    public void setLineWidthDisplay(double lineWidthDisplay, int channel) {
        this.lineWidthDisplay[channel] = lineWidthDisplay;
    }

    public double getDistanceToNail() {
        return distanceToNail;
    }

    public void setDistanceToNail(double distanceToNail) {
        this.distanceToNail = distanceToNail;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Color[] getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor, int channel) {
        this.lineColor[channel] = lineColor;
    }

    public String getGCodeFile() {
        return gCodeFile;
    }

    public void setGCodeFile(String gCodeFile) {
        this.gCodeFile = gCodeFile;
    }

    public int getZHop() {
        return zHop;
    }

    public void setZHop(int zHop) {
        this.zHop = zHop;
    }

    public int getSpeedCircle() {
        return speedCircle;
    }

    public void setSpeedCircle(int speedCircle) {
        this.speedCircle = speedCircle;
    }

    public int getSpeedTravel() {
        return speedTravel;
    }

    public void setSpeedTravel(int speedTravel) {
        this.speedTravel = speedTravel;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(int acceleration) {
        this.acceleration = acceleration;
    }

    public double getHeightStartingnail() {
        return heightStartingnail;
    }

    public void setHeightStartingnail(double heightStartingnail) {
        this.heightStartingnail = heightStartingnail;
    }

    public double getCoordinateXRight() {
        return coordinateXRight;
    }

    public void setCoordinateXRight(double coordinateXRight) {
        this.coordinateXRight = coordinateXRight;
    }

    public double getCoordinateYRight() {
        return coordinateYRight;
    }

    public void setCoordinateYRight(double coordinateYRight) {
        this.coordinateYRight = coordinateYRight;
    }

    public double getCoordinateXLeft() {
        return coordinateXLeft;
    }

    public void setCoordinateXLeft(double coordinateXLeft) {
        this.coordinateXLeft = coordinateXLeft;
    }

    public double getCoordinateYLeft() {
        return coordinateYLeft;
    }

    public void setCoordinateYLeft(double coordinateYLeft) {
        this.coordinateYLeft = coordinateYLeft;
    }

    public double getCompensationAngle() {
        return compensationAngle;
    }

    public void setCompensationAngle(double compensationAngle) {
        this.compensationAngle = compensationAngle;
    }

    public double getAbsoluteDistance() {
        return absoluteDistance;
    }

    public void setAbsoluteDistance(double absoluteDistance) {
        this.absoluteDistance = absoluteDistance;
    }

    public String getTextOutputStrings() {
        return textOutputStrings;
    }

    public void setTextOutputStrings(String textOutputStrings) {
        this.textOutputStrings = textOutputStrings;
    }
    
}
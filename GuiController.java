import javafx.application.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;

import javafx.scene.image.WritableImage;
/**
 * Beschreiben Sie hier die Klasse GuiController.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class GuiController extends Application
{
    private Data data = new Data();
    private Queue<int[]> lineOrder = new Queue();
    private StringArtPlotter stringArtPlotter = new StringArtPlotter();
    private ImageToArray imageToArray = new ImageToArray(data, lineOrder, stringArtPlotter);
    private StringartGenerator stringartGen = new StringartGenerator(data, lineOrder, stringArtPlotter);
    private GcodeGenerator gcodeGen = new GcodeGenerator(data, lineOrder, stringArtPlotter);
    private Main main = new Main(data, lineOrder, stringArtPlotter, imageToArray, stringartGen, gcodeGen);
    private HeatmapGen heatmapGen = new HeatmapGen();
    private GraphicsContext gc;

    //---------- 1. Seite ----------
    @FXML
    private ImageView imageView;
    @FXML    
    private ImageView convertedImage;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button refresh;
    @FXML
    private Tab stringartTab;
    @FXML
    private Tab gcodeTab;

    //---------- 2. Seite ----------
    @FXML
    private Spinner<Integer> spinnerMaxIterations;
    @FXML
    private Slider sliderCurrentIteration;
    @FXML
    private Label labelCurrentIteration;
    @FXML
    private Spinner<Integer> spinnerAnzahlNaegel;
    @FXML
    private Label labelNagelabstand;
    @FXML 
    private Spinner<Integer> spinnerDurchmesser;
    @FXML
    private Label labelLinienbreite;
    @FXML
    private Slider sliderLinienbreite;
    @FXML
    private Label labelLinienstaerke;
    @FXML
    private Slider sliderLinienstaerke;
    @FXML 
    private Label labelLinienbreiteAnzeige;
    @FXML
    private Slider sliderLinienbreiteAnzeige;
    @FXML
    private ColorPicker colorPickerHintergrund;
    @FXML
    private ColorPicker colorPickerLinie;
    @FXML
    private TextArea textAreaAusgabe;
    @FXML
    private Canvas canvas;
    @FXML
    private Rectangle rectangleBGCanvas;
    @FXML
    private ProgressBar progressbarStringGenerator;
    @FXML
    private Button generateArt;

    //---------- 3. Seite ----------
    @FXML
    private TextField textFieldDateiname;
    @FXML
    private Spinner spinnerZHop;
    @FXML
    private Spinner spinnerGeschwKurve;
    @FXML
    private Spinner spinnerGeschwTravel;
    @FXML
    private Spinner spinnerBeschleunigung;
    @FXML
    private Spinner spinnerAbstandNaegel;
    @FXML
    private Spinner spinnerAbstandX;
    @FXML
    private Spinner spinnerAbstandY;
    @FXML
    private TextArea textAreaVorschau;

    //---------- Initialisierung ----------
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("GUI.fxml")); 
        primaryStage.setTitle("StringArt Generator");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void main(String[] args) {
        launch(args);
    }

    @FXML
    public void initialize() {

        // Spinner konfiguration

        //---------- 2. Seite ----------
        SpinnerValueFactory<Integer> maxIterationsFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 5000);
        spinnerMaxIterations.setValueFactory(maxIterationsFactory);
        sliderCurrentIteration.setMax(spinnerMaxIterations.getValue());

        initIntegerSpinner(spinnerAnzahlNaegel, 2, 1000, 150);

        initIntegerSpinner(spinnerDurchmesser, 0, 1000, 0);

        //---------- 3. Seite ----------
        initIntegerSpinner(spinnerZHop, 0, 1000, 0);

        initIntegerSpinner(spinnerGeschwKurve, 0, 10000, 0);

        initIntegerSpinner(spinnerGeschwTravel, 0, 10000, 0);

        initIntegerSpinner(spinnerBeschleunigung, 0, 100000, 0);

        initIntegerSpinner(spinnerAbstandNaegel, 0, 10, 0);

        initIntegerSpinner(spinnerAbstandX, 0, 100, 0);

        initIntegerSpinner(spinnerAbstandY, 0, 100, 0);

        eventListeners();
        bindings();
        stringartProgress();
    }

    private void initIntegerSpinner(Spinner<Integer> spinner, int min, int max, int startwert) {

        SpinnerValueFactory<Integer> factory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, startwert);
        spinner.setValueFactory(factory);
    }

    @FXML
    public void eventListeners()
    {   
        //---------- 2. Seite ----------

        //setMaxIterations
        spinnerMaxIterations.valueProperty().addListener((obs, oldValue, newValue) -> {

                    // Maximalwert des Sliders setzen
                    sliderCurrentIteration.setMax(newValue);
                    data.setMaxIterations(newValue);

                    // Falls der Slider gerade über dem Limit liegt → heruntersetzen
                    if (sliderCurrentIteration.getValue() > newValue) {
                        sliderCurrentIteration.setValue(newValue);
                    }
            });

        // setCurrentIteration nur bei loslassen!!
        sliderCurrentIteration.valueProperty().addListener((obs, oldVal, newVal) -> {
                    // Benutzer hat den Slider losgelassen
                    data.setCurrentIteration((int) sliderCurrentIteration.getValue());
                    drawLines();

            });

        //setNails
        spinnerAnzahlNaegel.valueProperty().addListener((obs, oldValue, newValue) -> {

                    main.nailPositions(newValue);
                    double abstand = main.setAbstand();
                    labelNagelabstand.setText("Nagelabstand: " + abstand + " mm");
                    if(abstand <= 4.0 || abstand >= 1000000)
                    {
                        textAreaAusgabe.setText("Fehler: \n Nagelabstand darf 4mm nicht unterschreiten!");
                        generateArt.setDisable(true);
                    }
                    else
                    {
                        generateArt.setDisable(false);
                        textAreaAusgabe.setText("");
                    }
            });

        //setScale
        spinnerDurchmesser.valueProperty().addListener((obs, oldValue, newValue) -> {

                    main.setScale(newValue);
                    double abstand = main.setAbstand();
                    labelNagelabstand.setText("Nagelabstand: " + abstand + " mm");
                    if(abstand <= 4.0 || abstand >= 1000000)
                    {
                        textAreaAusgabe.setText("Fehler: \n Nagelabstand darf 4mm nicht unterschreiten!");
                        generateArt.setDisable(true);
                    }
                    else
                    {
                        generateArt.setDisable(false);
                        textAreaAusgabe.setText("");
                    }
            });

        //setlineWidth
        sliderLinienbreite.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setLineWidth((double)newVal)
        );

        //setlineStrength
        sliderLinienstaerke.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setLineStrength((double)newVal)
        );

        //setLineWidthDisplay
        sliderLinienbreiteAnzeige.valueProperty().addListener((obs, oldVal, newVal) ->{
                    data.setLineWidthDisplay((double)newVal);
                    drawLines();
            });

        //setBackgroundColor
        colorPickerHintergrund.valueProperty().addListener((obs, oldVal, newVal) -> {
                    data.setBackgroundColor(newVal);
                    rectangleBGCanvas.setFill(newVal);
            });

        //setLineColor
        colorPickerLinie.valueProperty().addListener((obs, oldVal, newVal) ->{
                    data.setLineColor(newVal);
                    drawLines();
            });

            
        //---------- 3. Seite ----------
        
        //setlineWidth
        spinnerZHop.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setZHop((int)newVal)
        );
        
        //setlineWidth
        spinnerGeschwKurve.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setSpeedCircle((int)newVal)
        );
        
        //setlineWidth
        spinnerGeschwTravel.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setSpeedTravel((int)newVal)
        );
        
        //setlineWidth
        spinnerBeschleunigung.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setAcceleration((int)newVal)
        );
        
        //setlineWidth
        spinnerAbstandNaegel.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setRadiusNails((int)newVal)
        );
        
        //setlineWidth
        spinnerAbstandX.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setDistanceX((int)newVal)
        );
        
        //setlineWidth
        spinnerAbstandY.valueProperty().addListener((obs, oldVal, newVal) ->
                data.setDistanceY((int)newVal)
        );
        
    }

    @FXML
    public void bindings()
    {
        bindSliderToLabel(sliderCurrentIteration, labelCurrentIteration, "%.0f");
        bindSliderToLabel(sliderLinienbreite, labelLinienbreite, "Linienbreite: %.3f");
        bindSliderToLabel(sliderLinienstaerke, labelLinienstaerke, "Linienstärke: %.3f");
        bindSliderToLabel(sliderLinienbreiteAnzeige, labelLinienbreiteAnzeige, "Linienbreite: %.3f");
    }

    //---------- 1. Seite ----------
    @FXML
    public void chooseFile()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Datei auswählen");

        // Optional: Filter für Dateitypen
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Bilder", "*.png")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            System.out.println("Ausgewählte Datei: " + file.getAbsolutePath());
            // Weiterverarbeiten...
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            data.setDateiName(file.getAbsolutePath());
            imageToArray.main();
        }

        if(data.getBildArray() != null)
        {
            generateHeatmap();
            main.nailPositions(data.getNails());

            refresh.setDisable(false);
            scrollPane.setDisable(false);
            stringartTab.setDisable(false);
        }
        else
        {
            refresh.setDisable(true);
            scrollPane.setDisable(true);
            stringartTab.setDisable(true);
        }
    }

    @FXML
    public void generateHeatmap()
    {
        WritableImage heatmap = heatmapGen.createHeatmap(data.getBildArray());
        convertedImage.setImage(heatmap);
    }

    //---------- 2. Seite ----------
    @FXML
    public void bindSliderToLabel(Slider slider, Label label, String format)
    {
        slider.valueProperty().addListener((obs, oldVal, newVal) ->
                label.setText(String.format(format, newVal.doubleValue()))
        );
    }

    @FXML
    public void generateArray()
    {
        // progressbarStringGenerator.setProgress(-1);
        generateArt.setDisable(true);
        stringartGen.main();
        drawLines();
        generateArt.setDisable(false);
        // progressbarStringGenerator.setProgress(0);
    }

    @FXML
    public void drawLines()
    { // Sicherung einbauen, dass keine Parameter null sind
        int[][] lineOrderArray = data.getlineOrderArray();
        double[][] nailCoords = data.getNailCoords();
        int currentIteration = data.getCurrentIteration();
        Color lineColor = data.getLineColor();
        double lineWidthDisplay = data.getLineWidthDisplay();
        gc = canvas.getGraphicsContext2D();

        double scaleX = canvas.getWidth() / data.getWidth();
        double scaleY = canvas.getHeight() / data.getHeight();

        if(lineOrderArray != null && nailCoords != null && currentIteration > 0 && lineWidthDisplay > 0.0)
        {
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            for(int i = 0; i < currentIteration && i < lineOrderArray.length; i++)
            {
                double x1 = nailCoords[lineOrderArray[i][0]][0] * scaleX;
                double x2 = nailCoords[lineOrderArray[i][1]][0] * scaleX;
                double y1 = nailCoords[lineOrderArray[i][0]][1] * scaleY;
                double y2 = nailCoords[lineOrderArray[i][1]][1] * scaleY;

                if (gc != null) {
                    gc.setStroke(lineColor);
                    gc.setLineWidth(lineWidthDisplay);
                    gc.strokeLine(x1, y1, x2, y2); // Zeichnet die neue Linie direkt auf den Canvas
                }
            }

            gcodeTab.setDisable(false);

        }
        else
        {
            gcodeTab.setDisable(true);
        }

        textAreaAusgabe.setText("Faldenlänge: " + (Math.round(main.calculateStringLength() / 10.0)/100.0) + " m");
    }

    public void stringartProgress()
    {
        // stringartGen.setProgressListener(progress -> {
        // progressbarStringGenerator.setProgress(progress);
        // System.out.println("update" + progress);
        // if(progress == 1)
        // {
        // progressbarStringGenerator.setProgress(0);
        // }
        // });
    }

    //---------- 3. Seite ----------
}

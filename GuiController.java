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
import javafx.scene.control.TextArea;

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
    private Main main = new Main(data);
    private HeatmapGen heatmapGen = new HeatmapGen();

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
    private Slider sliderlinienbreiteAnzeige;
    @FXML
    private ColorPicker colorPickerHintergrund;
    @FXML
    private ColorPicker colorPickerLinie;
    @FXML
    private TextArea textAreaAusgabe;

    //---------- 3. Seite ----------
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

        SpinnerValueFactory<Integer> anzahlNaegelFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 1000, 0);
        spinnerAnzahlNaegel.setValueFactory(anzahlNaegelFactory);

        SpinnerValueFactory<Integer> durchmesserFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000, 0);
        spinnerDurchmesser.setValueFactory(durchmesserFactory);

        eventListeners();
    }

    @FXML
    public void eventListeners()
    {   
        //spinnerMaxIterations
        spinnerMaxIterations.valueProperty().addListener((obs, oldValue, newValue) -> {

                    // Maximalwert des Sliders setzen
                    sliderCurrentIteration.setMax(newValue);

                    // Falls der Slider gerade über dem Limit liegt → heruntersetzen
                    if (sliderCurrentIteration.getValue() > newValue) {
                        sliderCurrentIteration.setValue(newValue);
                    }
            });

        // sliderCurrentIteration nur bei loslassen!!
        sliderCurrentIteration.valueChangingProperty().addListener((obs, wasChanging, isChanging) -> {
                    if (!isChanging) {
                        // Benutzer hat den Slider losgelassen
                        data.setCurrentIteration((int) sliderCurrentIteration.getValue());
                        System.out.println(data.getCurrentIteration() + "");
                    }
            });

        //spinnerAnzahlNaegel
        spinnerAnzahlNaegel.valueProperty().addListener((obs, oldValue, newValue) -> {

                    data.setNails(newValue);
                    labelNagelabstand.setText("Nagelabstand: " + main.setAbstand() + " mm");
            });

        //spinnerDurchmesser
        spinnerDurchmesser.valueProperty().addListener((obs, oldValue, newValue) -> {

                    main.setScale(newValue);
                    labelNagelabstand.setText("Nagelabstand: " + main.setAbstand() + " mm");
            });
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
            main.imageToArray();
        }

        if(data.getBildArray() != null)
        {
            generateHeatmap();

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
        System.out.println("IMG " + heatmap);
        convertedImage.setImage(heatmap);
    }

    //---------- 2. Seite ----------

}

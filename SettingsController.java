import javafx.application.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;

/**
 * Beschreiben Sie hier die Klasse SettingsController.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class SettingsController
{
    private Data data;
    private Main main;
    private Stage stage;

    private int anzahlNaegel;
    private double abstand;
    private double durchmesser;
    private double durchmesserSpitze;
    private double durchmesserNagel;
    private boolean update = false;

    @FXML
    private TextArea textAreaInfo;
    @FXML
    private Spinner<Integer> spinnerAnzahlNaegel;
    @FXML
    private Spinner<Double> spinnerAbstand;
    @FXML
    private Spinner<Integer> spinnerDurchmesser;
    @FXML
    private Spinner<Double> spinnerDurchmesserSpitze;
    @FXML
    private Spinner<Double> spinnerDurchmesserNagel;
    @FXML
    private Button buttonAbbruch;
    @FXML
    private Button buttonFertig;
    @FXML
    private ComboBox comboBoxForm;
    @FXML
    private ComboBox comboBoxModus;

    /**
     * Konstruktor für Objekte der Klasse SettingsController
     */
    public SettingsController(Data pData, Main pMain)
    {
        data = pData;
        main = pMain;
    }

    public void initialize()
    {

        initIntegerSpinner(spinnerAnzahlNaegel, 4, 1000, 150);

        initDoubleSpinner(spinnerAbstand, 0, 1000, 1);

        initIntegerSpinner(spinnerDurchmesser, 1, 1000, 1);

        initDoubleSpinner(spinnerDurchmesserSpitze, 0.1, 5, 0.5);

        initDoubleSpinner(spinnerDurchmesserNagel, 0.1, 5, 0.5);

        comboBoxModus.getItems().addAll("Schwarz", "Schwarzweiß", "CMYK");

        eventListeners();
        setUIValues();
    }

    @FXML
    public void eventListeners()
    {

        spinnerAnzahlNaegel.valueProperty().addListener((obs, oldValue, newValue) -> {

                    anzahlNaegel = newValue;

                    if(update) return;
                    update = true;
                    setDoubleSpinner(spinnerAbstand, (Math.PI * durchmesser) / anzahlNaegel);
                    update = false;

                    buttonFertig.setDisable(check());

            });

        spinnerAbstand.valueProperty().addListener((obs, oldValue, newValue) -> {

                    abstand = newValue;

                    if(update) return;
                    update = true;
                    int anzahl = (int) Math.round((Math.PI * durchmesser) / abstand);
                    setIntSpinner(spinnerAnzahlNaegel, anzahl);
                    setDoubleSpinner(spinnerAbstand, (Math.PI * durchmesser) / anzahl);
                    update = false;

                    buttonFertig.setDisable(check());

            });

        spinnerDurchmesser.valueProperty().addListener((obs, oldValue, newValue) -> {

                    durchmesser = newValue;
                    setDoubleSpinner(spinnerAbstand, (Math.PI * durchmesser) / anzahlNaegel);

                    buttonFertig.setDisable(check());

            });

        spinnerDurchmesserSpitze.valueProperty().addListener((obs, oldValue, newValue) -> {

                    durchmesserSpitze = newValue;

                    buttonFertig.setDisable(check());

            });

        spinnerDurchmesserNagel.valueProperty().addListener((obs, oldValue, newValue) -> {

                    durchmesserNagel = newValue;

                    buttonFertig.setDisable(check());

            });

    }

    @FXML
    public void setUIValues()
    {
        setIntSpinner(spinnerAnzahlNaegel, data.getNails());

        setIntSpinner(spinnerDurchmesser, (int)data.getDiameter());

        setDoubleSpinner(spinnerDurchmesserSpitze, data.getPinWidth());

        setDoubleSpinner(spinnerDurchmesserNagel, data.getNailWidth());

        comboBoxModus.getSelectionModel().select(data.getColorMode());
    }

    //---------- Hilfsmethoden ----------

    private void initIntegerSpinner(Spinner<Integer> spinner, int min, int max, int startwert) {

        SpinnerValueFactory<Integer> factory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, startwert);
        spinner.setValueFactory(factory);
    }

    private void initDoubleSpinner(Spinner<Double> spinner, double min, double max, double startwert) {

        SpinnerValueFactory<Double> factory =
            new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, startwert);
        spinner.setValueFactory(factory);
    }

    @FXML
    private void setIntSpinner(Spinner<Integer> spinner, int value) {
        // System.out.println("spinnerint" + spinner + value);
        if(spinner == null || spinner.getValueFactory() == null) return;
        spinner.getValueFactory().setValue(value);
    }

    @FXML
    private void setDoubleSpinner(Spinner<Double> spinner, double value) {
        // System.out.println("spinnerdouble" + spinner + value);
        if(spinner == null || spinner.getValueFactory() == null) return;
        spinner.getValueFactory().setValue(value);
    }

    @FXML
    private boolean check()
    {
        boolean distanceCorrect = (abstand <= durchmesserNagel + durchmesserSpitze + 0.5 || abstand >= 1000000);
        if(distanceCorrect)
            textAreaInfo.setText("Fehler: \n Nagelabstand darf " + (durchmesserNagel + durchmesserSpitze + 0.5) + " mm nicht unterschreiten!");
        else
            textAreaInfo.setText("");

        return distanceCorrect;
    }

    //---------- Logik ----------

    // public double Abstand()
    // {
    // data.setDistanceToNail((Math.PI * data.getDiameter()) / data.getNails());
    // return (Math.PI * data.getDiameter()) / data.getNails();
    // }

    public void overwriteData()
    {
        data.setNails(spinnerAnzahlNaegel.getValue());
        data.setNailDistance(spinnerAbstand.getValue());
        data.setDiameter(spinnerDurchmesser.getValue());
        data.setMmProPixel(spinnerDurchmesser.getValue() / data.getWidth());
        data.setPinWidth(spinnerDurchmesserSpitze.getValue());
        data.setNailWidth(spinnerDurchmesserNagel.getValue());
        data.setColorMode(comboBoxModus.getItems().indexOf(comboBoxModus.getValue()));
        main.nailPositions(data.getNails());
    }

    public void cancel()
    {
        stage = (Stage) spinnerAnzahlNaegel.getScene().getWindow();
        stage.close();
    }

    public void save()
    {
        overwriteData();

        stage = (Stage) spinnerAnzahlNaegel.getScene().getWindow();
        stage.close();
    }
}
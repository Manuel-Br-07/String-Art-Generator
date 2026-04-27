import javafx.application.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.collections.ObservableList;

/**
 * Beschreiben Sie hier die Klasse StringVisibilityController.
 * 
 * @author (Ihr Name) 
 * @version (eine Versionsnummer oder ein Datum)
 */
public class StringVisibilityController
{
    private Data data;
    private Main main;
    private Stage stage;

    private boolean update = false;
    private boolean saved = false;

    public StringVisibilityController(Data pData, Main pMain){
        data = pData;
        main = pMain;
    }

    // public void cancel()
    // {
        // saved = false;
        // stage = (Stage) spinnerAnzahlNaegel.getScene().getWindow();
        // stage.close();
    // }

    // public void save()
    // {
        // overwriteData();

        // saved = true;
        // stage = (Stage) spinnerAnzahlNaegel.getScene().getWindow();
        // stage.close();
    // }

}
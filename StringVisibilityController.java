import javafx.application.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

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
    
    public StringVisibilityController(Data pData, Main pMain){
        data = pData;
        main = pMain;
    }
    
}
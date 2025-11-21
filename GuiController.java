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
    
    @FXML
    private ImageView imageView;
    
    
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

    }
}


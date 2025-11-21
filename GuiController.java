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
}


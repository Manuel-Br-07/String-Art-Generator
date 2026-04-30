import javafx.application.Platform;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import javafx.scene.control.*;

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
    private int[] order;

    @FXML
    private ListView<String> listViewColors;

    public StringVisibilityController(Data pData, Main pMain){
        data = pData;
        main = pMain;
    }

    public void initialize()
    {
        order = data.getColorMapping()[data.getColorMode()];
        writeDisplay(-1);
    }

    public boolean isSaved()
    {
        return saved;
    }

    public void moveUp()
    {
        moveColor(-1);
    }

    public void moveDown()
    {
        moveColor(1);
    }

    //---------- Logik ----------

    public void moveColor(int direction)
    {
        int index = listViewColors.getSelectionModel().getSelectedIndex();
        if (index < 0) return;

        int newIndex = index + direction;
        if (newIndex < 0 || newIndex >= order.length) return;

        int temp = order[newIndex];
        order[newIndex] = order[index];
        order[index] = temp;

        writeDisplay(newIndex);
    }

    public void writeDisplay(int pIndex)
    {
        String[] colors = new String[order.length];

        for(int i = 0; i < order.length; i++)
            colors[i] = data.getColorNames()[order[i]];

        listViewColors.getItems().setAll(colors);
        if(pIndex != -1)
            listViewColors.getSelectionModel().select(pIndex);
    }

    public void overwriteData()
    {
        data.setColorMapping(order, data.getColorMode());
    }

    public void cancel()
    {
        saved = false;
        stage = (Stage) listViewColors.getScene().getWindow();
        stage.close();
    }

    public void save()
    {
        overwriteData();

        saved = true;
        stage = (Stage) listViewColors.getScene().getWindow();
        stage.close();
    }

}
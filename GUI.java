import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.geometry.*;
import javafx.collections.*;
import javafx.scene.canvas.*;
import javafx.scene.input.*;
import javafx.event.Event;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.input.KeyCombination;

/**
 *
 * Beschreibung
 *
 * @version 1.0 vom 08.11.2025
 * @author 
 */

public class GUI extends Application {
  // start attributes
  private Canvas canvas1 = new Canvas();
  // end attributes
  
  public void start(Stage primaryStage) { 
    Pane root = new Pane();
    Scene scene = new Scene(root, 1034, 617);
    // start components
    
    canvas1.setLayoutX(88);
    canvas1.setLayoutY(240);
    canvas1.setWidth(120);
    canvas1.setHeight(80);
    root.getChildren().add(canvas1);
    root.getChildren().add(menuBar1);



    // end components
    
    primaryStage.setOnCloseRequest(e -> System.exit(0));
    primaryStage.setTitle("GUI");
    primaryStage.setScene(scene);
    primaryStage.show();
  } // end of public GUI
  
  // start methods
  
  public static void main(String[] args) {
    launch(args);
  } // end of main
  


  // end methods
} // end of class GUI

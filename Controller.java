import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;

// partially from https://www.youtube.com/watch?v=cvcO4DvDVsA

public class Controller implements Initializable{

    public static Launch launch;

    // Load interface from fxml file
    @FXML
    public Slider alignSlider;
    @FXML
    public Slider cohesionSlider;
    @FXML
    public Slider seperationSlider;
    @FXML
    public Slider visionRangeSlider;
    @FXML
    public Slider numBoidsSlider;
    @FXML
    public Slider speedSlider;
    @FXML
    public CheckBox edgeAvoidance;
    @FXML
    public CheckBox debugCircles;
    @FXML
    public CheckBox debugLines;
    @FXML
    public CheckBox shadows;
    @FXML
    public CheckBox triangles;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alignSlider.valueProperty().addListener(new ChangeListener<Number>() {
        
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.alignmentMult = alignSlider.getValue();
            }
        });
        cohesionSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.cohesionMult = cohesionSlider.getValue();
            }
        });
        seperationSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.seperationMult = seperationSlider.getValue();
            }
        });
        visionRangeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.visionRange = visionRangeSlider.getValue();
            }
        });
        numBoidsSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.numBoids = (int) numBoidsSlider.getValue();
            }
        });
        speedSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
                launch.speed = (int) speedSlider.getValue();
            }
        });

        edgeAvoidance.setOnAction(e ->{
            if(edgeAvoidance.isSelected()){
                launch.avoidEdges = true;
            }else{
                launch.avoidEdges = false;
            }
        } );
        debugCircles.setOnAction(e ->{
            if(debugCircles.isSelected()){
                launch.drawDebugCircles = true;
            }else{
                launch.drawDebugCircles = false;
            }
        } );
        debugLines.setOnAction(e ->{    
            if(debugLines.isSelected()){
                launch.debugLines = true;
            }else{
                launch.debugLines = false;
            }
        } );
        shadows.setOnAction(e ->{    
            if(shadows.isSelected()){
                launch.shadows = true;
            }else{
                launch.shadows = false;
            }
        } );
        triangles.setOnAction(e ->{    
            if(triangles.isSelected()){
                launch.triangles = true;
            }else{
                launch.triangles = false;
            }
        } );
    }

    @FXML
	public void browser() throws IOException, URISyntaxException{
		String url = "https://github.com/cs-olympic/finalcs1-7fi";
        Desktop.getDesktop().browse(new URI(url));
	}
    @FXML
    public void reset(){
        launch.resetBoids();
    }
}
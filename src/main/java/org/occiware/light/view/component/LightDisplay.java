package org.occiware.light.view.component;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;

/**
 * Light container to display with a traffic light within, usage of a vertical
 * tile pane.
 *
 * @author Christophe Gourdin - INRIA
 */
public class LightDisplay extends GridPane {

    private TrafficLight trafLight;
    private Label locationLabel = new Label();
    private Label idLabel = new Label();

    /**
     * Build a light to display.
     *
     * @param id
     * @param location
     */
    public LightDisplay(String id, String location) {
        // Build a light.
        // trafLight = new TrafficLight(200, 200, 100, 200);
        trafLight = new TrafficLight(200, 200, 100, 200);
        if (id == null) {
            this.idLabel.setText("");
        } else {
            this.idLabel.setText(id);
        }
        if (location == null) {
            this.locationLabel.setText("");
        } else {
            this.locationLabel.setText(location);
        }
        idLabel.setTextFill(Color.RED);
        locationLabel.setTextFill(Color.RED);
        
        // GridPane attributes.
        
        this.setAlignment(Pos.TOP_LEFT);
        this.setHgap(5);
        this.setVgap(5);
//        this.setTileAlignment(Pos.CENTER_LEFT);
//        this.setOrientation(Orientation.VERTICAL);
//        this.setPrefColumns(1);
//        this.setPrefRows(3);
        
        
        this.setPrefHeight(USE_COMPUTED_SIZE);
        this.setPrefWidth(USE_COMPUTED_SIZE);
        
        this.add(trafLight, 0, 0);
        
        this.add(idLabel, 0, 1);
        this.add(locationLabel, 0, 2);
        
    }

    public Label getLocationLabel() {
        return locationLabel;
    }

    public Label getIdLabel() {
        return idLabel;
    }
    
    /**
     * Set the light on or off.
     * @param state 
     */
    public void setLightState(Light state) {
        this.trafLight.setLightState(state);
        if (Light.OFF.equals(state)) {
            // update color of labels.
            this.idLabel.setTextFill(Color.RED);
            this.locationLabel.setTextFill(Color.RED);
        } else if (Light.ON.equals(state)) {
            this.idLabel.setTextFill(Color.BLACK);
            this.locationLabel.setTextFill(Color.BLACK);
        }
    }

}

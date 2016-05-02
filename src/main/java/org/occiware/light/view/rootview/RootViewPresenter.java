package org.occiware.light.view.rootview;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.occiware.light.TowerControl;
import org.occiware.light.exception.LightActionException;
import org.occiware.light.server.LightServerEndpoint;
import org.occiware.light.view.component.Light;
import org.occiware.light.view.component.LightDisplay;
import org.occiware.light.view.component.TrafficLight;

/**
 *
 * @author christophe
 */
public class RootViewPresenter implements Initializable {

    private static final Logger log = Logger.getLogger(RootViewPresenter.class);

    @FXML
    private TilePane tilePane;

    private boolean isLoaded = false;
    private boolean isJobApplicationInitiated = false;

    @Inject
    private TowerControl tower;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @PostConstruct
    public void init() {
        log.debug("init RootViewPresenter class");
        isLoaded = true;

    }

    public TilePane getRootTilePane() {
        return tilePane;
    }

    public void addTrafficLightToRootPane() {
        if (isLoaded) {
            // We start the server here.
            tower.startServer();
            LightServerEndpoint.presenter = this;
        }
    }

    /**
     * Build and display a new light.
     *
     * @param id
     * @param location
     * @throws LightActionException
     */
    public void createLight(final String id, final String location) throws LightActionException {
        LightDisplay light;
        try {
            // if Light already exist, we update location only.
            this.updateLightLocation(id, location);
            return;
        } catch (LightActionException ex) {
            // No op here.
        }

        light = new LightDisplay(id, location);
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    // Add the light to main TilePane.
                    tilePane.getChildren().add(light);
                }
            });

        } catch (Exception e) {
            throw new LightActionException(e.getMessage());
        }
    }

    /**
     * Delete a light from display and free alloc resources.
     *
     * @param id
     * @throws LightActionException
     */
    public void deleteLight(final String id) throws LightActionException {
        LightDisplay light = findLightById(id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Remove from light from display.
                tilePane.getChildren().remove(light);
            }
        });

    }

    /**
     * Update location of a light.
     *
     * @param id
     * @param location
     * @throws LightActionException
     */
    public void updateLightLocation(final String id, final String location) throws LightActionException {
        LightDisplay light = findLightById(id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                light.getLocationLabel().setText(location);
            }
        });
    }

    /**
     * Update the light status. (on or off)
     *
     * @param id (unique id of the light.
     * @param light
     * @throws org.occiware.light.exception.LightActionException
     */
    public void updateLightState(String id, Light light) throws LightActionException {
        LightDisplay lightDisplay;
        lightDisplay = findLightById(id);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Get the lightDisplay object and update his state.
                if (tilePane.getChildren() != null && !tilePane.getChildren().isEmpty()) {
                    lightDisplay.setLightState(light);
                }
            }
        });
    }

    /**
     * Find a lightDisplay on tilePane.
     *
     * @param id
     * @return A LightDisplay object found on tilePane, if not found a
     * lightActionException is thrown.
     * @throws org.occiware.light.exception.LightActionException
     */
    public LightDisplay findLightById(final String id) throws LightActionException {
        LightDisplay lightDisplay = null;
        LightDisplay lightDisplayTmp;

        ObservableList<Node> nodes = tilePane.getChildren();
        if (nodes != null && !tilePane.getChildren().isEmpty()) {
            try {
                for (Node node : nodes) {
                    lightDisplayTmp = (LightDisplay) node;
                    if (lightDisplayTmp.getIdLabel().getText().equals(id)) {
                        lightDisplay = lightDisplayTmp;
                    }
                }

            } catch (Exception e) {
                throw new LightActionException(e.getMessage());
            }
        }
        if (lightDisplay == null) {
            throw new LightActionException("Light not found with this id : " + id);
        }
        return lightDisplay;

    }
    /**
     * Retrieve a light with it's id.
     * @param id
     * @return a light with format : id;location;State
     * @throws org.occiware.light.exception.LightActionException
     */
    public String retrieveLight(final String id) throws LightActionException {
        LightDisplay light = findLightById(id);
        Light state = light.getLightState();
        String location = light.getLocationLabel().getText();
        String msg = id + ";" + location + ";" + state.name().toLowerCase();
        log.info(msg);
        return msg;
    }

    /**
     * Stop the server.
     */
    public void stopServer() {
        tower.stopServer();
    }

}

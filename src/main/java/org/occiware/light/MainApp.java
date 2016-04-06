/** 
 * Copyright 2016 - Christophe Gourdin
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.occiware.light;

import com.airhacks.afterburner.injection.Injector;
import java.util.Date;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javax.websocket.DeploymentException;
import org.apache.log4j.Logger;
import org.occiware.light.server.LightServer;
import org.occiware.light.view.rootview.RootViewPresenter;
import org.occiware.light.view.rootview.RootViewView;

/**
 * Main application class.
 * @author Christophe Gourdin
 */
public class MainApp extends Application {

    private static final Logger log = Logger.getLogger(MainApp.class);
    private RootViewPresenter presenter;
    private RootViewView rootView;
    
    
    @Override
    public void start(Stage stage) throws Exception {
        log.info("Starting lightserverdemo on " + new Date());
        log.debug("Showing the scene");

        final Screen primaryScreen = Screen.getPrimary();
        Rectangle2D visualBounds = primaryScreen.getVisualBounds();
        double width = visualBounds.getWidth() - 10;
        double height = visualBounds.getHeight() - 10;
        rootView = new RootViewView();

        presenter = (RootViewPresenter) rootView.getPresenter();
        presenter.addTrafficLightToRootPane();
        
        Parent rootViewParent = rootView.getView();
        
        Scene scene = new Scene(rootViewParent, width, height);
        
        stage.setTitle("LightServerDemo Project");
        stage.setScene(scene);
        stage.show();
        
        
    }

    // private static final Logger log = Logger.getLogger(MainApp.class);
    public static void main(String[] args) throws Exception {
        // Test pour corriger un bug dans les versions de javapackager avant la version 8u40.
        // Lors du passage à la v8u40, il faudra tester sans.
        // Et la remplacer par cette ligne : // launch(args);
        // Initialise les appenders des loggers.
        LoggerConfig.initAppenders();
        launch(args);
        // com.sun.javafx.application.LauncherImpl.launchApplication(MainApp.class, PreloaderMain.class, args);
    }

    @Override
    public void stop() throws Exception {
        presenter.stopServer();
        Injector.forgetAll();
        super.stop();
        
    }

    @Override
    public void init() throws Exception {
        super.init();
        
    }

}

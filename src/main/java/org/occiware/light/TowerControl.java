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

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import javax.annotation.PostConstruct;
import javax.websocket.DeploymentException;
import org.apache.log4j.Logger;
import org.occiware.light.server.LightServer;
import org.occiware.light.server.LightServerEndpoint;
import org.occiware.light.view.component.Light;
import org.occiware.light.view.global.View;
import org.occiware.light.view.rootview.RootViewPresenter;

/**
 * This class is injected on all views.
 * @author christophe
 */
public class TowerControl {
    private static final Logger log = Logger.getLogger(TowerControl.class);
    private static final char NEWLINE = '\n';
    private static final String SPACE_SEPARATOR = " ";
    
    public static final String ROOT_VIEWID = "rootview";
    
    /**
     * RootView of the application.
     */
    private View rootView;
    
    private Stage mainStage;
    private Application application;
    private LightServer server;
    
    
    @PostConstruct
    public void init() {

    }
    
    public String readyToTakeoff() {
        return "ok from tower";
    }
    
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public void startServer() {
        // Start.
        server = new LightServer();
        try {
            server.start();
            
        } catch (DeploymentException ex) {
            log.error("Cant start the websocket server, stopping the application.");
            Platform.exit();
        }
        
    }
    
    public void stopServer() {
        server.stop();
    }
    
}

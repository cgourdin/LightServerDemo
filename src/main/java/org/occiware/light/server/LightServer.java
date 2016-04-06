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
package org.occiware.light.server;

import javax.websocket.DeploymentException;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A websocket server that handle light command on and off, other messages are ignored.
 * @author Christophe Gourdin
 */
public class LightServer {
    private static final Logger log = LoggerFactory.getLogger(LightServer.class);
    private static final String SERVER_HOSTNAME = "localhost";
    private static final int SERVER_PORT = 8025;
    private static final String SERVER_CONTEXT_PATH = "/websocket";
    
    private Server server;
    
    public static final String SERVER_ADDRESS =
            "ws://" + SERVER_HOSTNAME + ":" + SERVER_PORT + SERVER_CONTEXT_PATH;
    
    public void start() throws DeploymentException {
        try {
            log.info("Starting server for " + SERVER_ADDRESS);

            server = new Server(
                    SERVER_HOSTNAME,
                    SERVER_PORT,
                    SERVER_CONTEXT_PATH,
                    null,
                    LightServerEndpoint.class
            );

            server.start();
            // TODO : Set state ihm server started.
            
        } catch (DeploymentException e) {
            server = null;
            throw e;
        }
    }
    
    /**
     * Shuts down the server.
     */
    public void stop() {
        if (server != null) {
            log.info("Stopping server for " + SERVER_ADDRESS);
            // TODO : Set state ihm server stopped. 
            server.stop();
        }
    }
    
    
}

/**
 * Copyright 2016 - Christophe Gourdin
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.occiware.light.server;

import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.occiware.light.exception.LightActionException;
import org.occiware.light.view.component.Light;
import org.occiware.light.view.rootview.RootViewPresenter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A websocket server that respond to Light On and Off command.
 *
 * @author christophe
 */
@ServerEndpoint(value = "/light")
public class LightServerEndpoint {

    private static final Logger log = LoggerFactory.getLogger(LightServerEndpoint.class);

    public static RootViewPresenter presenter;

    /**
     * On received, this set the light to on or off state.
     *
     * @param session the current websocket session.
     * @param message a name to which the server should respond hello to.
     * @return the response string "ok" or a message if error was found.
     */
    @OnMessage
    public String onMessage(Session session, String message) {
        log.debug("LightServer received request for: " + message + " being processed for session " + session.getId());
        // Decode the message, separator ; used to split the id and the action to realize.
        // Format : id (unique);action command (create, delete, switchOn, switchOff, updateLocation);location if any or updated location.
        String[] messageArray = message.split(";");
        String id = null;
        String action = null;
        String location = null;
        String messageReturn = "ok";
        switch (messageArray.length) {
            case 3:
                id = messageArray[0];
                action = messageArray[1];
                location = messageArray[2];
                break;
            case 2:
                id = messageArray[0];
                action = messageArray[1];
                location = "";
                // messageReturn = "warning : no location provided";
                break;
            default:
                messageReturn = "Error, it's not possible to execute a light action.";
                return messageReturn;
        }
        if (id == null) {
            messageReturn = "Error, no unique id provided for the light";
            return messageReturn;
        }
        if (action == null) {
            messageReturn = "Error, no action provided !";
            return messageReturn;
        }
        
        try {
            // Update state on jfx thread.
            switch (action) {
                case "create":
                    presenter.createLight(id, location);
                    break;
                case "delete":
                    presenter.deleteLight(id);
                    break;
                case "switchOn":
                    presenter.updateLightState(id, Light.ON);
                    break;
                case "switchOff":
                    presenter.updateLightState(id, Light.OFF);
                    break;
                case "updateLocation":
                    presenter.updateLightLocation(id, location);
                    break;
                case "retrieve":
                    messageReturn = presenter.retrieveLight(id);
                    break;
                    
            }
        } catch (LightActionException ex) {
            messageReturn = "Error, Server cannot execute the action : " + action + " --> cause: " + ex.getMessage();
        }

        return messageReturn;
    }

    /**
     * Logs an error if one is detected.
     *
     * @param session the websocket session to which is in error.
     * @param throwable the detected error to be logged.
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("LightServer encountered error for session " + session.getId(), throwable);
    }

}

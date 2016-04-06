package org.occiware.light.view.global;


import javafx.scene.Parent;

/**
 * Interface for all the views. Cette interface permet d'interconnecter les
 * écrans entre eux.
 *
 * @author christophe
 */
public interface View {

    public Object getPresenter();

    public Parent getView();

}

package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class Constructor extends ClassComponent {

    private ArrayList<Parameter> parameters;

    public Constructor(String modifier, String name) {
        this.modifier = modifier;
        this.name = name;
    }

    public Constructor(String modifier, String name, ArrayList<Parameter> parameters) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = parameters;
    }

    @Override
    public String toString() {
        return modifier + " " + name;
    }

    @Override
    public HBox getDisplay() {
        return null;
    }
}

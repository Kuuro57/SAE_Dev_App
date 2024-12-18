package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class Method extends ClassComponent {
    private String returnType;
    private ArrayList<Parameter> parameters;

    public Method(String modifier, String name, String returnType) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
    }

    public Method(String modifier, String name, ArrayList<Parameter> parameters, String returnType) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return modifier + " " + returnType + " " + name + "(" + parameters.toString() + ")";
    }

    @Override
    public HBox getDisplay() {
        return null;
    }
}

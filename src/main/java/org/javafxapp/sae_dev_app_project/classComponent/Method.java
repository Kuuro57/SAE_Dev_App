package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

public class Method extends ClassComponent {
    private String returnType;
    private String parameters;

    public Method(String modifier, String name, String returnType, String parameters) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public String getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return modifier + " " + returnType + " " + name + "(" + parameters + ")";
    }

    @Override
    public HBox getDisplay() {
        return null;
    }
}

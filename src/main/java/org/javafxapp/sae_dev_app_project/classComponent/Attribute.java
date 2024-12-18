package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

public class Attribute extends ClassComponent {
    private String type;

    public Attribute(String modifier, String name, String type) {
        this.modifier = modifier;
        this.name = name;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return modifier + " " + type + " " + name;
    }

    @Override
    public HBox getDisplay() {
        return null;
    }
}

package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

public abstract class ClassComponent {
    protected String modifier;
    protected String name;

    public String getName() {
        return name;
    }

    public String getModifier() {
        return modifier;
    }

    public abstract HBox getDisplay();
}

package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;


/**
 * Class qui représente un attribut d'une classe
 */
public class Attribute extends ClassComponent {

    // Attributs
    private String type; // Type de l'attribut



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès à l'attribut
     * @param name Nom de l'attribut
     * @param type Type de l'attribut
     */
    public Attribute(String modifier, String name, String type) {
        this.modifier = modifier;
        this.name = name;
        this.type = type;
    }



    @Override
    public HBox getDisplay() {
        return null;
    }



    /*
     * ### GETTERS ###
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return modifier + " " + type + " " + name;
    }

}

package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

import java.lang.reflect.Parameter;
import java.util.ArrayList;


/**
 * Classe qui représente un constructeur d'une classe
 */
public class Constructor extends ClassComponent {

    // Attributs
    private ArrayList<Parameter> parameters; // Liste des paramètres du constructeur



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès du constructeur
     * @param name Nom du constructeur
     */
    public Constructor(String modifier, String name) {
        this.modifier = modifier;
        this.name = name;
    }



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès du constructeur
     * @param name Nom du constructeur
     * @param parameters Liste des paramètres du constructeur
     */
    public Constructor(String modifier, String name, ArrayList<Parameter> parameters) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = parameters;
    }



    @Override
    public HBox getDisplay() {
        return null;
    }



    @Override
    public String toString() {
        return modifier + " " + name;
    }

}

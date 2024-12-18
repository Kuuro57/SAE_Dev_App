package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;

import java.lang.reflect.Parameter;
import java.util.ArrayList;


/**
 * Classe qui représente une méthode d'une classe
 */
public class Method extends ClassComponent {

    // Attributs
    private String returnType; // Type de retour de la méthode
    private ArrayList<Parameter> parameters; // Liste des paramètres de la méthode



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès de la méthode
     * @param name Nom de la méthode
     * @param returnType Type de retour de la méthode
     * @param parameters Liste des paramètres de la méthode
     */
    public Method(String modifier, String name, String returnType, ArrayList<Parameter> parameters) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
    }



    @Override
    public HBox getDisplay() {
        return null;
    }



    /*
     * ### GETTERS ###
     */
    public String getReturnType() { return returnType; }
    public ArrayList<Parameter> getParameters() { return parameters; }



    @Override
    public String toString() {
        return modifier + " " + returnType + " " + name + "(" + parameters.toString() + ")";
    }

}

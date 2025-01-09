package org.javafxapp.sae_dev_app_project.classComponent;


/**
 * Classe qui représente les paramètres d'une méthode ou d'un constructeur
 */
public class Parameter {

    // Attributs
    private String name; // Nom du paramètre
    private String type; // Type du paramètre



    /**
     * Constructeur de la classe
     * @param type Type du paramètre
     * @param name Nom du paramètre
     */
    public Parameter(String type, String name) {
        this.type = type;
        this.name = name;
    }



    /*
     * ### GETTERS ###
     */
    public String getName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }



    public String toString() {
        return this.type + " " + this.name;
    }

}

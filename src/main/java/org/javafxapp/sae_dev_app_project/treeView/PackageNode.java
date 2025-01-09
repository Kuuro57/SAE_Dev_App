package org.javafxapp.sae_dev_app_project.treeView;

/**
 * Classe qui représente un noeud de package
 */
public class PackageNode {

    // Attributs
    private String name; // Nom du package



    /**
     * Constructeur de la classe
     * @param name Nom du package
     */
    public PackageNode(String name) {
        this.name = name;
    }



    /*
     * ### GETTERS ###
     */
    public String getName() {
        return name;
    }



    @Override
    public String toString() {
        return name;
    }

}

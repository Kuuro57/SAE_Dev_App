package org.javafxapp.sae_dev_app_project.treeView;

/**
 * Classe qui représente un noeud de package
 */
public class PackageNode {

    /**
     * Attributs
     * Name : Nom du package
     * Path : Chemin du package
     */
    private String name;
    private String path;

    /**
     * Constructeur de la classe
     * @param name Nom du package
     * @param path Chemin du package
     */
    public PackageNode(String name, String path) {
        this.name = name;
        this.path = path;
    }

    /*
     * ### GETTERS ###
     */
    public String getName() {
        return name;
    }

    /**
     * Méthode qui retourne le nom du package
     * @return Le nom du package
     */
    @Override
    public String toString() {
        return name;
    }

}

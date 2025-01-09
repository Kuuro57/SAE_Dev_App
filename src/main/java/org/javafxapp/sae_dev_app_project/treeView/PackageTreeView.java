package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeView;


/**
 * Classe qui représente l'arbre des packages
 */
public class PackageTreeView {


    // Attributs
    private final TreeView<PackageNode> treeView; // Arbre des packages



    /**
     * Constructeur de la classe
     */
    public PackageTreeView() {
        this.treeView = new TreeView<>();
    }



    /**
     * Méthode qui retourne l'arbre des packages
     * @return L'arbre des packages
     */
    public TreeView<PackageNode> getTreeView() {
        return treeView;
    }



    /**
     * Méthode qui crée l'arbre des packages
     */
    public void initialize(DragAndDropHandler dragHandler) {
        dragHandler.setup(treeView);
    }

}

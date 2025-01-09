package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeView;
import org.javafxapp.sae_dev_app_project.importExport.Import;

/**
 * Classe qui représente l'arbre des packages
 */
public class PackageTreeView {

    /**
     * Attributs
     * treeView : Arbre des packages
     */
    private final TreeView<PackageNode> treeView;

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
     * @return L'arbre des packages
     */
    public void initialize(DragAndDropHandler dragHandler) {
        dragHandler.setup(treeView);
    }

}

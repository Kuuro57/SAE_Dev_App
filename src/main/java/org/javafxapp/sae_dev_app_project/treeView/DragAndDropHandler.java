package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;


/**
 * Classe qui gère le drag and drop des classes depuis l'arbre des packages
 */
public class DragAndDropHandler {

    // Attributs
    private final ViewAllClasses targetView; // Vue de toutes les classes



    /**
     * Constructeur de la classe
     * @param targetView Vue de toutes les classes
     */
    public DragAndDropHandler(ViewAllClasses targetView) {
        this.targetView = targetView;
    }



    /**
     * Méthode qui initialise le drag and drop
     * @param packageTree
     */
    public void setup(TreeView<PackageNode> packageTree) {
        packageTree.setOnDragDetected(event -> handleDragDetected(event, packageTree));
        targetView.setOnDragOver(this::handleDragOver);
        targetView.setOnDragDropped(this::handleDragDropped);
    }



    /**
     * Méthode qui gère le drag detected
     * @param event
     * @param packageTree
     */
    private void handleDragDetected(MouseEvent event, TreeView<PackageNode> packageTree) {
        TreeItem<PackageNode> selectedItem = packageTree.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem.isLeaf()) {
            Dragboard dragboard = packageTree.startDragAndDrop(TransferMode.COPY);
            ClipboardContent content = new ClipboardContent();
            content.putString(selectedItem.getValue().getName());
            dragboard.setContent(content);
            event.consume();
        }
    }



    /**
     * Méthode qui gère le drag over
     * @param event
     */
    private void handleDragOver(DragEvent event) {
        if (event.getGestureSource() != targetView && event.getDragboard().hasString()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }



    /**
     * Méthode qui gère le drag dropped
     * @param event
     */
    private void handleDragDropped(DragEvent event) {
        boolean success = false;
        Dragboard dragboard = event.getDragboard();
        if (dragboard.hasString()) {
            String className = dragboard.getString();
            ModelClass modelClass = new ModelClass(className); // Crée directement une classe pour simplifier
            modelClass.setX((int) event.getX());
            modelClass.setY((int) event.getY());
            modelClass.addObserver(targetView);
            targetView.addClass(modelClass);
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

}
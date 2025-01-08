package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

/**
 * Classe qui gère le drag and drop des classes depuis l'arbre des packages
 */
public class DragAndDropHandler {

    /**
     * Attributs
     * view : Vue de toutes les classes
     */
    private final ViewAllClasses view;

    /**
     * Constructeur de la classe
     * @param view Vue de toutes les classes
     */
    public DragAndDropHandler(ViewAllClasses view) {
        this.view = view;
    }

    /**
     * Méthode qui initialise le drag and drop
     * @param treeView Arbre des packages
     */
    public void setupDragAndDrop(TreeView<PackageNode> treeView) {

        // Drag and drop depuis l'arbre des packages
        treeView.setOnDragDetected(event -> {
            TreeItem<PackageNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
            // Si l'élément sélectionné est une feuille, on commence le drag and drop
            if (selectedItem != null && selectedItem.isLeaf()) {
                Dragboard dragboard = treeView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedItem.getValue().getName());
                dragboard.setContent(content);
                event.consume();
            }
        });

        // Drag and drop sur la vue de toutes les classes
        view.setOnDragOver(event -> {
            // Si l'élément drag and drop n'est pas la vue et qu'il contient une chaîne de caractères, on accepte le transfert
            if (event.getGestureSource() != view && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Drag and drop sur la vue de toutes les classes
        view.setOnDragDropped(event -> {
            boolean success = false;
            // Si l'élément drag and drop contient une chaîne de caractères, on récupère la classe correspondante
            if (event.getDragboard().hasString()) {
                String className = event.getDragboard().getString();
                ModelClass modelClass = Import.getModelClass(view, className);
                // Si la classe est non nulle, on l'ajoute à la vue
                if (modelClass != null) {
                    modelClass.addObserver(view);
                    view.addClass(modelClass);
                    success = true;
                }
            }
            // On termine le drag and drop
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.*;

import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

public class DragAndDropHandler {

    private final ViewAllClasses view;

    public DragAndDropHandler(ViewAllClasses view) {
        this.view = view;
    }

    public void setupDragAndDrop(TreeView<PackageNode> treeView) {

        treeView.setOnDragDetected(event -> {
            TreeItem<PackageNode> selectedItem = treeView.getSelectionModel().getSelectedItem();
            if (selectedItem != null && selectedItem.isLeaf()) {
                Dragboard dragboard = treeView.startDragAndDrop(TransferMode.COPY);
                ClipboardContent content = new ClipboardContent();
                content.putString(selectedItem.getValue().getName());
                dragboard.setContent(content);
                event.consume();
            }
        });

        view.setOnDragOver(event -> {
            if (event.getGestureSource() != view && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });


        view.setOnDragDropped(event -> {
            boolean success = false;
            if (event.getDragboard().hasString()) {
                String className = event.getDragboard().getString();
                ModelClass modelClass = Import.getModelClass(view, className);
                if (modelClass != null) {
                    view.addClass(modelClass);
                    view.update();
                    success = true;
                }
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }
}
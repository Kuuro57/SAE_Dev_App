package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;
import org.javafxapp.sae_dev_app_project.importExport.Import;

public class PackageTreeView {

    private TreeView<PackageNode> treeView;

    public PackageTreeView() {
        treeView = new TreeView<>();
    }

    public TreeView<PackageNode> getTreeView() {
        return treeView;
    }

    public void addFile(String fileName, String path) {
        TreeItem<PackageNode> file = new TreeItem<>(new PackageNode(fileName, path));
        treeView.getRoot().getChildren().add(file);
    }

    public TreeView<PackageNode> createPackageTreeView() {

        TreeView<PackageNode> treeView = new TreeView<>();
        Import.setTreeView(treeView);
        return treeView;
    }

    private void setupDragAndDrop() {
        treeView.setCellFactory(tv -> {
            TreeCell<PackageNode> cell = new TreeCell<>() {
                @Override
                protected void updateItem(PackageNode item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getName());
                    }
                }
            };

            cell.setOnDragDetected(event -> {
                if (cell.getItem() == null) {
                    return;
                }

                Dragboard dragboard = cell.startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(cell.getItem().getName());
                dragboard.setContent(content);
                event.consume();
            });

            return cell;
        });
    }
}

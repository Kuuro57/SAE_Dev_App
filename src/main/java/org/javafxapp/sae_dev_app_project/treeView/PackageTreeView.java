package org.javafxapp.sae_dev_app_project.treeView;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class PackageTreeView {

    private TreeView<PackageNode> treeView;
    private TreeItem<PackageNode> rootItem;

    public PackageTreeView() {
        rootItem = new TreeItem<>(new PackageNode("Root", ""));
        treeView = new TreeView<>(rootItem);
    }

    public TreeView<PackageNode> getTreeView() {
        return treeView;
    }

    public void addFile(String fileName, String path) {
        TreeItem<PackageNode> file = new TreeItem<>(new PackageNode(fileName, path));
        rootItem.getChildren().add(file);
    }

    public TreeView<PackageNode> createPackageTreeView() {
        TreeItem<PackageNode> root = new TreeItem<>(new PackageNode("Root", ""));

        //package qui vient du filechooser
        TreeItem<PackageNode> package1 = new TreeItem<>(new PackageNode("package1", ""));
        TreeItem<PackageNode> package2 = new TreeItem<>(new PackageNode("package2", ""));
        TreeItem<PackageNode> subPackage1 = new TreeItem<>(new PackageNode("subPackage1", ""));

        root.getChildren().addAll(package1, package2);
        package1.getChildren().add(subPackage1);
        root.setExpanded(true);

        TreeView<PackageNode> treeView = new TreeView<>(root);
        treeView.setCellFactory(new Callback<TreeView<PackageNode>, TreeCell<PackageNode>>() {
            @Override
            public TreeCell<PackageNode> call(TreeView<PackageNode> packageNodeTreeView) {
                return new TreeCell<PackageNode>() {
                    @Override
                    protected void updateItem(PackageNode packageNode, boolean empty) {
                        super.updateItem(packageNode, empty);
                        if (packageNode != null) {
                            setText(packageNode.toString());
                            setStyle(" -fx-background-color: grey ; ");
                        } else {
                            setText("");
                            setStyle("");
                        }
                    }
                };
            }

        });
        return treeView;
    }
}

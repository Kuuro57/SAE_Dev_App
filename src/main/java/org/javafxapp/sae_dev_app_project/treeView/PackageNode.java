package org.javafxapp.sae_dev_app_project.treeView;

public class PackageNode {
    private String name;
    private String path;

    public PackageNode(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return name;
    }

}

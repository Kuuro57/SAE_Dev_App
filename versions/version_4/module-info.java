module org.javafxapp.sae_dev_app_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;


    opens org.javafxapp.sae_dev_app_project to javafx.fxml;
    exports org.javafxapp.sae_dev_app_project;
}
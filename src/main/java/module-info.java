module org.javafxapp.sae_dev_app_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;


    opens org.javafxapp.sae_dev_app_project to javafx.fxml;
    exports org.javafxapp.sae_dev_app_project;
    exports org.javafxapp.sae_dev_app_project.subjects;
    exports org.javafxapp.sae_dev_app_project.classComponent;
    exports org.javafxapp.sae_dev_app_project.importExport;
    exports org.javafxapp.sae_dev_app_project.menuHandler;
    exports org.javafxapp.sae_dev_app_project.views;



    
}

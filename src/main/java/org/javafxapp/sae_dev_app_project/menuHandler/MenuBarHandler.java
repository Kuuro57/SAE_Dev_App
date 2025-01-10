package org.javafxapp.sae_dev_app_project.menuHandler;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.views.ClassCreator;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.FileNotFoundException;


/**
 * Classe qui permet de créer le menu en haut de l'application
 */
public class MenuBarHandler {


    /**
     * Méthode qui créé un objet MenuBar
     * @param stage Stage de la page JavaFX
     * @param view Vue qui contient toutes les classes du diagrammes
     * @return Un objet MenuBar
     */
    public MenuBar createMenuBar(Stage stage, ViewAllClasses view) {

        // Initialisation des menus principaux
        MenuBar menuBar = new MenuBar();
        Menu fileMenu = new Menu("Fichier");
        Menu editionMenu = new Menu("Edition");
        Menu showMenu = new Menu("Affichage");
        Menu helpMenu = new Menu("Aide");


        // Initialisation des items du menu "Fichier>Importer"
        Menu importMenu = new Menu("Importer");

        MenuItem importClassItem = new MenuItem("Importer une classe");
        importClassItem.setOnAction(actionEvent -> {
            try {
                Import.importClass(view);
            }
            catch (FileNotFoundException e) {
                displayError("Erreur lors de l'importation de la classe !\nMessage : " + e.getMessage());
            }
        });

        MenuItem importPackageItem = new MenuItem("Importer un projet");
        importPackageItem.setOnAction(actionEvent -> {
            try {
                Import.importPackage();
            }
            catch (Exception e) {
                displayError("Erreur lors de l'importation du package !\nMessage : " + e.getMessage());
            }
        });

        MenuItem exitItem = new MenuItem("Quitter");
        exitItem.setOnAction(actionEvent -> stage.close());


        // Ajout des items au menu correspondant
        importMenu.getItems().addAll(importClassItem, importPackageItem);



        // Initialisation des items du menu "Fichier>Exporter"
        Menu exportMenu = new Menu("Exporter");

        MenuItem exportPNG = new MenuItem("Format PNG");
        exportPNG.setOnAction(actionEvent -> Export.exportInPNG(view));

        MenuItem exportPUMLCode = new MenuItem("Format code PlantUML");
        exportPUMLCode.setOnAction(actionEvent -> Export.exportInPUml(view));

        MenuItem exportJavaTemplate = new MenuItem("Format squelette Java");
        exportJavaTemplate.setOnAction(actionEvent -> Export.exportInJava(view));


        // Ajout des items au menu correspondant
        exportMenu.getItems().addAll(exportPNG, exportPUMLCode, exportJavaTemplate);



        // Initialisation des items du menu "Edition"
        MenuItem addClassItem = new MenuItem("Créer une classe");
        addClassItem.setOnAction(actionEvent -> ClassCreator.getInstance(view).classCreation());



        MenuItem hideAllClassAttributes = new MenuItem("Masquer les attributs des classes");
        hideAllClassAttributes.setOnAction(actionEvent -> {
            view.hideAttributes();
        });

        MenuItem showAllClassAttributes = new MenuItem("Afficher les attributs des classes");
        showAllClassAttributes.setOnAction(actionEvent -> {
            view.showAttributes();
        });

        MenuItem hideAllClassMethods = new MenuItem("Masquer les méthodes des classes");
        hideAllClassMethods.setOnAction(actionEvent -> {
            view.hideMethods();
        });

        MenuItem showAllClassMethods = new MenuItem("Afficher les méthodes des classes");
        showAllClassMethods.setOnAction(actionEvent -> {
            view.showMethods();
        });

        MenuItem hideAllClassConstructors = new MenuItem("Masquer les constructeurs des classes");
        hideAllClassConstructors.setOnAction(actionEvent -> {
            view.hideConstructors();
        });

        MenuItem showAllClassConstructors = new MenuItem("Afficher les constructeurs des classes");
        showAllClassConstructors.setOnAction(actionEvent -> {
            view.showConstructors();
        });

        MenuItem showAllClassesHiddenItem = new MenuItem("Réafficher les classes cachées");
        showAllClassesHiddenItem.setOnAction(actionEvent -> {
            view.showAllHiddenClasses();
        });



        // Ajout des sous menus aux menus principaux
        fileMenu.getItems().addAll(importMenu, exportMenu, exitItem);
        editionMenu.getItems().addAll(addClassItem);
        showMenu.getItems().addAll(showAllClassAttributes, hideAllClassAttributes, new SeparatorMenuItem(), showAllClassConstructors, hideAllClassConstructors, new SeparatorMenuItem(), showAllClassMethods, hideAllClassMethods, new SeparatorMenuItem(), showAllClassesHiddenItem);

        menuBar.getMenus().addAll(fileMenu, editionMenu, showMenu, helpMenu);

        // On retourne le menu bar
        return menuBar;
    }



    /**
     * Méthode qui affiche une pop up d'alerte lors d'une erreur
     * @param msg Messaque que l'on veut afficher dans le pop up
     */
    private void displayError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(msg);
        alert.show();
    }



}

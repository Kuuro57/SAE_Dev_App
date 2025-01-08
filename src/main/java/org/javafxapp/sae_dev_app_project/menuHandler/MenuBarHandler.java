package org.javafxapp.sae_dev_app_project.menuHandler;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.FileNotFoundException;

public class MenuBarHandler {

    /**
     * Méthode qui créé une menuBar
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
                Import.importPackage(view);
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

        MenuItem exportPUMLImage = new MenuItem("Format image PlantUML");
        // TODO Ajouter l'action correspondante !

        MenuItem exportJavaTemplate = new MenuItem("Format squelette Java");
        exportJavaTemplate.setOnAction(actionEvent -> Export.exportInJava(view));


        // Ajout des items au menu correspondant
        exportMenu.getItems().addAll(exportPNG, exportPUMLCode, exportPUMLImage, exportJavaTemplate);



        // Initialisation des items du menu "Edition"
        MenuItem addClassItem = new MenuItem("Créer une classe");
        //addClassItem.setOnAction(actionEvent -> ModelClass.classCreator());



        MenuItem createPackageItem = new MenuItem("Créer un package");
        // TODO Ajouter l'action correspondante !



        // Initialisation des items du menu "Edition>Ajouter"
        Menu addMenu = new Menu("Ajouter");

        MenuItem addDependenciesItem = new MenuItem("Dépendance");
        // TODO Ajouter l'action correspondante !

        MenuItem addAttributeItem = new MenuItem("Attribut");
        // TODO Ajouter l'action correspondante !

        MenuItem addMethodItem = new MenuItem("Méthode");
        // TODO Ajouter l'action correspondante !

        MenuItem addConstructorItem = new MenuItem("Constructeur");
        // TODO Ajouter l'action correspondante !


        // Ajout des items au menu correspondant
        addMenu.getItems().addAll(addDependenciesItem, addAttributeItem, addMethodItem, addConstructorItem);



        // Initialisation des items du menu "Edition>Modifier"
        Menu modifyMenu = new Menu("Modifier");

        MenuItem modifyAttributeItem = new MenuItem("Attribut");
        // TODO Ajouter l'action correspondante !

        MenuItem modifyMethodItem = new MenuItem("Méthode");
        // TODO Ajouter l'action correspondante !

        MenuItem modifyConstructorItem = new MenuItem("Constructeur");
        // TODO Ajouter l'action correspondante !


        // Ajout des items au menu correspondant
        modifyMenu.getItems().addAll(modifyAttributeItem, modifyMethodItem, modifyConstructorItem);




        // Initialisation des items du menu "Edition>Supprimer"
        Menu removeMenu = new Menu("Supprimer");

        MenuItem removeAttributeItem = new MenuItem("Attribut");
        // TODO Ajouter l'action correspondante !

        MenuItem removeMethodItem = new MenuItem("Méthode");
        // TODO Ajouter l'action correspondante !

        MenuItem removeConstructorItem = new MenuItem("Constructeur");
        // TODO Ajouter l'action correspondante !


        // Ajout des items au menu correspondant
        removeMenu.getItems().addAll(removeAttributeItem, removeMethodItem, removeConstructorItem);


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



        // Ajout des sous menus aux menus principaux
        fileMenu.getItems().addAll(importMenu, exportMenu, exitItem);
        editionMenu.getItems().addAll(addMenu, modifyMenu, removeMenu, new SeparatorMenuItem(), addClassItem, createPackageItem);
        showMenu.getItems().addAll(showAllClassAttributes, hideAllClassAttributes, new SeparatorMenuItem(), showAllClassConstructors, hideAllClassConstructors, new SeparatorMenuItem(), showAllClassMethods, hideAllClassMethods);

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

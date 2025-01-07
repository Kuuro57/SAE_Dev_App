package org.javafxapp.sae_dev_app_project.menuBar;

import javafx.scene.control.*;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

public class MenuBarHandler {



    public MenuBar createMenuBar(Stage stage, ViewAllClasses view){


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
            catch (ClassNotFoundException e) {
                displayError("Erreur lors de l'importation de la classe ! (Classe non trouvée)");
            }
        });

        MenuItem importPackageItem = new MenuItem("Importer un projet");
        importPackageItem.setOnAction(actionEvent -> {
            try {
                Import.importPackage(view);
            }
            catch (ClassNotFoundException e) {
                displayError("Erreur lors de l'importation du package ! (Classe non trouvée)");
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
        // TODO Ajouter l'action correspondante !

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




        // Initialisation des items du menu "Affichage"
        MenuItem displayAttributesAllClassesItem = new MenuItem("Afficher les attributs de toutes les classes");
        // TODO Ajouter l'action correspondante !

        MenuItem displayMethodsAllClassesItem = new MenuItem("Afficher les méthodes de toutes les classes");
        // TODO Ajouter l'action correspondante !





        // Ajout des sous menus aux menus principaux
        fileMenu.getItems().addAll(importMenu, exportMenu, exitItem);
        editionMenu.getItems().addAll(addMenu, modifyMenu, removeMenu, new SeparatorMenuItem(), addClassItem, createPackageItem);
        showMenu.getItems().addAll(displayAttributesAllClassesItem, displayMethodsAllClassesItem);
        menuBar.getMenus().addAll(fileMenu, editionMenu, showMenu, helpMenu);

        // On retourne la menu bar
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
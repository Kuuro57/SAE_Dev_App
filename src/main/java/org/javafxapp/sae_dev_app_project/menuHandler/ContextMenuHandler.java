package org.javafxapp.sae_dev_app_project.menuHandler;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Menu;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

public class ContextMenuHandler {

    /**
     * Méthode qui crée et fait apparaître un menu contextuel convivial pour une classe
     * @param view Vue représentant la zone graphique du diagramme
     * @param model Le modèle associé à la classe
     * @return Un menu contextuel convivial
     */
    public static ContextMenu createClassContextMenu(ViewAllClasses view, ModelClass model) {

        // Initialisation du ContextMenu principal
        ContextMenu contextMenu = new ContextMenu();

        // Sous-menu pour les attributs
        Menu attributeMenu = new Menu("Attributs");
        MenuItem hideAttributesItem = new MenuItem("Masquer");
        hideAttributesItem.setOnAction(actionEvent -> {
            model.hideAllAttributes();
            view.update();
        });
        MenuItem showAttributesItem = new MenuItem("Afficher");
        showAttributesItem.setOnAction(actionEvent -> {
            model.showAllAttributes();
            view.update();
        });
        attributeMenu.getItems().addAll(hideAttributesItem, showAttributesItem);

        // Sous-menu pour les méthodes
        Menu methodMenu = new Menu("Méthodes");
        MenuItem hideMethodsItem = new MenuItem("Masquer");
        hideMethodsItem.setOnAction(actionEvent -> {
            model.hideAllMethods();
            view.update();
        });
        MenuItem showMethodsItem = new MenuItem("Afficher");
        showMethodsItem.setOnAction(actionEvent -> {
            model.showAllMethods();
            view.update();
        });
        methodMenu.getItems().addAll(hideMethodsItem, showMethodsItem);

        // Sous-menu pour les constructeurs
        Menu constructorMenu = new Menu("Constructeurs");
        MenuItem hideConstructorsItem = new MenuItem("Masquer");
        hideConstructorsItem.setOnAction(actionEvent -> {
            model.hideConstructors();
            view.update();
        });
        MenuItem showConstructorsItem = new MenuItem("Afficher");
        showConstructorsItem.setOnAction(actionEvent -> {
            model.showConstructors();
            view.update();
        });
        constructorMenu.getItems().addAll(hideConstructorsItem, showConstructorsItem);

        // Nouvelle option : Masquer tout sauf le titre
        MenuItem hideDetailsItem = new MenuItem("Masquer tout");
        hideDetailsItem.setOnAction(actionEvent -> {
            model.hideDetails();
            view.update();
        });

        // Option : Réinitialiser tout
        MenuItem resetAllItem = new MenuItem("Réinitialiser l'affichage");
        resetAllItem.setOnAction(actionEvent -> {
            model.showAllAttributes();
            model.showAllMethods();
            model.showConstructors();
            view.update();
        });

        // Option : Masquer la classe
        MenuItem hideClass = new MenuItem("Masquer la classe");
        hideClass.setOnAction(actionEvent -> {
            model.setVisibility(false);
            view.updateDependentAttributes(model);
        });


        // Structuration du menu contextuel : Ajout des sous-menus et options
        contextMenu.getItems().addAll(attributeMenu, methodMenu, constructorMenu, new SeparatorMenuItem(), hideDetailsItem, resetAllItem, new SeparatorMenuItem(), hideClass);

        return contextMenu;
    }
}
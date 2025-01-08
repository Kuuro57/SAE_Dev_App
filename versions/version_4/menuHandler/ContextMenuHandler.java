package org.javafxapp.sae_dev_app_project.menuHandler;

import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

public class ContextMenuHandler {


    /**
     * Méthode qui créé et fait apparaître un context menu pour une classe
     * @param view Vue qui représente la zone graphique du diagramme
     * @return Un context menu
     */
    public static ContextMenu createClassContextMenu (ViewAllClasses view, ModelClass model) {

        // Initialisation du ContextMenu
        ContextMenu contextMenu = new ContextMenu();

        // Bouton qui masque tous les attributs de la classe
        MenuItem hideAttributsItem = new MenuItem("Masquer tous les attributs");
        hideAttributsItem.setOnAction(actionEvent -> {
            model.hideAllAttributes();
            view.update();
        });
        contextMenu.getItems().add(hideAttributsItem);

        // Bouton qui affiche tous les attributs de la classe
        MenuItem displayAttributsItem = new MenuItem("Afficher tous les attributs");
        displayAttributsItem.setOnAction(actionEvent -> {
            model.showAllAttributes();
            view.update();
        });
        contextMenu.getItems().add(displayAttributsItem);

        // Bouton qui masque toutes les méthodes de la classe
        MenuItem hideMethodsItem = new MenuItem("Masquer toutes les méthodes");
        hideMethodsItem.setOnAction(actionEvent -> {
            model.hideAllMethods();
            view.update();
        });
        contextMenu.getItems().add(hideMethodsItem);

        // Bouton qui affiche toutes les méthodes de la classe
        MenuItem displayMethodsItem = new MenuItem("Afficher toutes les méthodes");
        displayMethodsItem.setOnAction(actionEvent -> {
            model.showAllMethods();
            view.update();
        });
        contextMenu.getItems().add(displayMethodsItem);

        // Bouton qui masque les constructeurs de la classe
        MenuItem hideConstructorsItem = new MenuItem("Masquer tous les constructeurs");
        hideConstructorsItem.setOnAction(actionEvent -> {
            model.hideConstructors();
            view.update();
        });
        contextMenu.getItems().add(hideConstructorsItem);

        // Bouton qui affiche les constructeurs de la classe
        MenuItem displayConstructorsItem = new MenuItem("Afficher tous les constructeurs");
        displayConstructorsItem.setOnAction(actionEvent -> {
            model.showConstructors();
            view.update();
        });
        contextMenu.getItems().add(displayConstructorsItem);


        return contextMenu;

    }

}

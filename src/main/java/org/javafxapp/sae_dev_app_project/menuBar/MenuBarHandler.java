package org.javafxapp.sae_dev_app_project.menuBar;

import javafx.geometry.Side;
import javafx.scene.control.*;

public class MenuBarHandler {

    public MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();

        Menu fichierMenu = new Menu("Fichier");
        Menu editionMenu = new Menu("Edition");
        Menu affichageMenu = new Menu("Affichage");
        Menu aideMenu = new Menu("Aide");

        menuBar.getMenus().addAll(fichierMenu, editionMenu, affichageMenu, aideMenu);

        //options de la contextmenu du menu fichier, ouvrir, importer, exporter qui aura une sous menu et quitter
        addContextMenu(fichierMenu, "Ouvrir", "Importer", "Exporter", "Quitter");
        addContextMenu(editionMenu, "Ajouter une classe", "Ajouter des dependances", "Ajouter un attribut a une classe existante", "Ajouter une methode a une classe existante", "Ajouter un constructeur a une classe existante", "Supprimer une classe", "Creer un package");

        return menuBar;
    }

    private void addContextMenu(Menu menu, String... items) {
        for (String item : items) {
            MenuItem menuItem = new MenuItem(item);
            menu.getItems().add(menuItem);
        }
    }
}

package org.javafxapp.sae_dev_app_project.menuBar;

import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

public class MenuBarHandler {


    public MenuBar createMenuBar(){
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("Fichier");
        Menu editionMenu = new Menu("Edition");
        Menu showMenu = new Menu("Affichage");
        Menu helpMenu = new Menu("Aide");

        menuBar.getMenus().addAll(fileMenu, editionMenu, showMenu, helpMenu);

        //options de la contextmenu du menu fichier, ouvrir, importer, exporter qui aura une sous menu et quitter
        addContextMenu(
                fileMenu,
                "Ouvrir",
                "-", //separateur
                "Importer depuis chemin",
                "Exporter",
                "-",
                "Quitter");



        //sous menu du menu fichier depuis exporter
        Menu exporterMenu = new Menu("Exporter");
        fileMenu.getItems().add(2, exporterMenu);
        addContextMenu(
                exporterMenu,
                "Format PNG",
                "Format PlantUML Code",
                "Format PlantUML Image",
                "Format Java Code");

        addContextMenu(
                editionMenu,
                "Ajouter une classe",
                "Ajouter des dépendances",
                "Ajouter un attribut a une classe existante",
                "Ajouter une methode a une classe existante",
                "Ajouter un constructeur a une classe existante",
                "-",
                "-",
                "Créer un package");
        // sousmenu depuis supprimer une classe, afficher dans ce sous menu les classes de l'application
        Menu supprimerClasseMenu = new Menu("Supprimer une classe");
        editionMenu.getItems().add(6, supprimerClasseMenu);

        addContextMenu(
                showMenu,
                "Afficher les attributs de toutes les classes",
                "Afficher les méthodes de toutes les classes",
                "-",
                "-");
        //sous menu depuis afficher les attributs de la classe, afficher dans ce sous menu les classes de l'application
        Menu afficherAttributsMenu = new Menu("Afficher les attributs de la classe");
        showMenu.getItems().add(3, afficherAttributsMenu);
        Menu afficherMethodesMenu = new Menu("Afficher les méthodes de la classe");
        showMenu.getItems().add(4, afficherMethodesMenu);
        Menu masquerAttributsMenu = new Menu("Masquer les attributs de la classe");
        showMenu.getItems().add(6, masquerAttributsMenu);
        Menu masquerMethodesMenu = new Menu("Masquer les méthodes de la classe");
        showMenu.getItems().add(7, masquerMethodesMenu);

        return menuBar;
    }

    /**
     * Ajoute des items à un menu
     * @param menu Menu
     * @param items Items
     */
    private void addContextMenu(Menu menu, String... items) {
        for (String item : items) {
            if (item.equals("-")) {
                menu.getItems().add(new SeparatorMenuItem());
            } else {
                MenuItem menuItem = new MenuItem(item);
                menu.getItems().add(menuItem);
            }
        }
    }


}

package org.javafxapp.sae_dev_app_project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.importExport.FileChooserHandler;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.importExport.CustomClassLoader;
import org.javafxapp.sae_dev_app_project.menuBar.MenuBarHandler;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;


public class Main extends Application {
    @Override
    public void start(Stage stage) throws ClassNotFoundException {

        // TODO Initialisation des élément du graphe de scene
        GridPane grid = new GridPane();
        ViewAllClasses graphicView = new ViewAllClasses();

        // Création MenuBar avec MenuBarHandler
        MenuBarHandler mbh = new MenuBarHandler();
        MenuBar menuBar = mbh.createMenuBar();



        // Ajout du menuBar en top, il doit occuper toute la largeur
        grid.add(menuBar, 0, 0);
        GridPane.setColumnSpan(menuBar, 2);

        // Configuration du GridPane
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(80);
        grid.getColumnConstraints().addAll(column1, column2);

        RowConstraints row1 = new RowConstraints();
        //setter l'ahuteur de la première ligne pour s'adapter pile a la taille du menuBar
        row1.setPercentHeight(5);
        RowConstraints row2 = new RowConstraints();
        row2.setPercentHeight(95);
        grid.getRowConstraints().addAll(row1, row2);


        grid.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.setAlignment(Pos.CENTER);

        // Configuration de la vue
        graphicView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        graphicView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, new Insets(0, 0, 0, 0))));
        graphicView.getStyleClass().add("grid-cell");

        // Initialisation des modèles
        ModelClass model = new ModelClass("boubou");
        ModelClass model2 = new ModelClass("bobo");

        FileChooserHandler fileChooserHandler = new FileChooserHandler(graphicView);
        // lier le menuItem importer a l'ouverture du fileChooser
        menuBar.getMenus().get(0).getItems().get(3).setOnAction(e -> fileChooserHandler.openFileChooser(stage));


        // On ajoute la vue au modèle pour qu'elle soit notifier du changement
        model.addObserver(graphicView);
        model2.addObserver(graphicView);


        // On ajoute les vues au Gridpane
        grid.add(graphicView, 1, 1);

        // On ajoute le modèle à la vue
        graphicView.addClass(model);
        graphicView.addClass(model2);
        model.notifyObservers();


        // Initialisation de la scène et affichage de la page
        Scene scene = new Scene(grid, 1000, 600);
        stage.setTitle("PlantUM'Aide ©");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}
package org.javafxapp.sae_dev_app_project;

import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;


public class Main extends Application {
    @Override
    public void start(Stage stage) {

        // TODO Initialisation des élément du graphe de scene
        GridPane grid = new GridPane();
        ViewAllClasses graphicView = new ViewAllClasses();


        // Configuration du GridPane
        grid.setGridLinesVisible(true);

        ColumnConstraints column1 = new ColumnConstraints();
        column1.setPercentWidth(20);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setPercentWidth(80);
        grid.getColumnConstraints().addAll(column1, column2);

        RowConstraints row1 = new RowConstraints();
        row1.setPercentHeight(100);
        grid.getRowConstraints().addAll(row1);

        grid.setPrefSize(Double.MAX_VALUE, Double.MAX_VALUE);
        grid.setAlignment(Pos.CENTER);


        // Configuration de la vue
        graphicView.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        graphicView.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, new Insets(0, 0, 0, 0))));




        // Initialisation des modèles
        ModelClass model = new ModelClass("boubou");
        ModelClass model2 = new ModelClass("bobo");

        // On ajoute la vue au modèle pour qu'elle soit notifier du changement
        model.addObserver(graphicView);

        // On ajoute les vues au Gridpane
        grid.add(graphicView, 1, 0);

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
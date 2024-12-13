package org.javafxapp.sae_dev_app_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.javafxapp.sae_dev_app_project.ImportExport.Export;
import org.javafxapp.sae_dev_app_project.ImportExport.Import;
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

        Button b = new Button("Enregistrer en PNG");
        b.setOnAction(e -> Export.exportInPNG(graphicView));
        grid.add(b, 0, 0);

        Button b2 = new Button("Importer une classe");
        b2.setOnAction(actionEvent -> {
            try {
                Import.importClass(graphicView);
            }
            catch (ClassNotFoundException e) {
                displayError("Erreur lors de l'importation du fichier. Veuillez réessayer");
            }
        });
        grid.add(b2, 0, 1);



        // On ajoute la vue au modèle pour qu'elle soit notifier du changement
        model.addObserver(graphicView);
        model2.addObserver(graphicView);


        // On ajoute les vues au Gridpane
        grid.add(graphicView, 1, 0);

        // On ajoute le modèle à la vue
        graphicView.addClass(model);
        graphicView.addClass(model2);








        // Initialisation de la scène et affichage de la page
        Scene scene = new Scene(grid, 1000, 600);
        stage.setTitle("PlantUM'Aide ©");
        stage.setScene(scene);
        stage.show();


    }



    /**
     * Méthode qui lance l'application
     */
    public static void main(String[] args) {
        launch();
    }



    /**
     * Méthode qui affiche une pop-up d'erreur avec le message en paramètre
     * @param msg Message que l'on veut afficher
     */
    private static void displayError(String msg) {

        // On affiche une alerte
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(msg);
        alert.show();

    }

}
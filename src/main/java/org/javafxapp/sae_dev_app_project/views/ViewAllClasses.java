package org.javafxapp.sae_dev_app_project.views;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.javafxapp.sae_dev_app_project.importExport.Import;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.util.ArrayList;

/**
 * Classe qui représente la vue qui contient toutes les classes représentées graphiquements
 */
public class ViewAllClasses extends Pane implements Observer {

    // Attributs
    private ArrayList<ModelClass> allClassesList; // Liste qui contient toutes les classes sur le diagramme


    /**
     * Constructeur de la classe
     */
    public ViewAllClasses() {
        this.allClassesList = new ArrayList<>(1000);
    }



    /**
     * Méthode qui ajoute une classe à la liste des classes représentées graphiquement
     * @param m Objet de type ModelClass que l'on veut ajouter à la liste
     * @return True si la classe à bien été ajoutée, false sinon
     */
    public boolean addClass(ModelClass m) {

        // Si la classe n'est pas déjà présente
        if (!this.allClassesList.contains(m)) {

            // On lui donne un nouvel id et on l'ajoute à la liste
            this.allClassesList.add(m);

            // On notifie l'observeur que le modèle est ajouté à la liste
            m.notifyObservers();

            return true;

        }

        // Sinon on retourne false
        return false;
    }


    @Override
    public void update() {

        // Si il y a des éléments sur le diagramme
        if (!this.getChildren().isEmpty()) {
            // On enlève toutes les classes et dépendances présentes sur le diagramme
            this.getChildren().remove(0, this.getChildren().size() - 1);
        }


        // On recharge toutes les classes
        this.reloadAllClasses();

        // On affiche les dépendances (qui seront derrière les classes)
        this.displayDependancies();

        // On boucle sur la liste des classes
        for (ModelClass m : this.allClassesList) {


            // On récupère l'affichage de la classe
            VBox display = m.getDisplay();


            // Action quand l'utilisateur appuie sur la classe
            display.setOnMousePressed(action -> {

                // On sélectionne ce modèle
                display.setBackground(new Background(new BackgroundFill(Color.RED, null, new Insets(0, 0, 0, 0))));
                m.toogleIsSelected();

            });

            // Action quand l'utilisateur relâche le clique
            display.setOnMouseReleased(action -> {

                ModelClass model = this.allClassesList.get(Integer.parseInt(display.getId()));

                // On désélectionne ce modèle
                display.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
                model.toogleIsSelected();

                // On récupère les coordonnées vis à vis de toute la page (pas seulement de la vue)
                Node parent = display.getParent();
                Point2D cooVBox = parent.sceneToLocal(action.getSceneX(), action.getSceneY()); // Transformation des coordonnées du clique pour que la VBox se positionne au bon endroit
                int coo_x = (int) (cooVBox.getX() - display.getWidth() / 2);
                int coo_y = (int) (cooVBox.getY() - display.getHeight() / 2);

                // On change ses coordonnées
                display.setLayoutX(coo_x);
                display.setLayoutY(coo_y);

                // On change les coordonnées du modèle (pour que la classe soit au milieu des coordonées du clique)
                model.setX(coo_x);
                model.setY(coo_y);

                // On met à jour la vue du diagramme
                this.update();

            });


            System.out.println(m.getName() + "(" + m.getX() + ", " + m.getY() + ") | id : " + m.getId());

            // On lui donnes les coordonnées
            display.setLayoutX(m.getX());
            display.setLayoutY(m.getY());

            // On l'ajoute sur le Pane
            this.getChildren().add(display);
        }



    }



    /**
     * Méthode qui recharge tous les modèles de la vue
     */
    private void reloadAllClasses() {

        // On boucle sur les classes présentes
        for (ModelClass m : this.allClassesList) {
            // On récupère un nouveau modèle
            ModelClass newM = Import.getModelClass(this, m.getName(), true);

            // On récupère son ancien ID et ses anciennes coordonnées
            newM.setId(m.getId());
            newM.setX(m.getX());
            newM.setY(m.getY());

            // On remplace l'ancien modèle par le nouveau
            this.allClassesList.set(m.getId(), newM);

        }

    }




    /**
     * Méthode qui affiche sur le diagramme les dépendances de toutes les classes
     */
    private void displayDependancies() {

        // On boucle sur les classes
        for (ModelClass m : this.allClassesList) {
            // On récupère les coordonnées x et y de la classe
            int coo_x = m.getX();
            int coo_y = m.getY();

            // On boucle sur les classes implémentées par cette classe
            for (ModelClass m_interface : m.getInheritedClasses()) {
                // On trace une ligne entre les deux classes
                Line line = new Line(coo_x, coo_y, m_interface.getX(), m_interface.getY());
                this.getChildren().add(line);
            }

            // Si la classe hérite d'une classe
            ModelClass m_herit = m.getExtendedClass();
            if (m_herit != null) {

                m_herit = this.allClassesList.get(m_herit.getId());

                System.out.println(m.getName() + "(" + m.getX() + ", " + m.getY() + ") extend ->" + m_herit.getName() + "(" + m_herit.getX() + ", " + m_herit.getY() + ")");

                // On trace une ligne entre les deux classes
                Line line = new Line(coo_x, coo_y, m_herit.getX(), m_herit.getY());
                this.getChildren().add(line);
            }
        }

    }



    /*
     * ### GETTERS ###
     */
    public ArrayList<ModelClass> getAllClasses() { return this.allClassesList; }

}

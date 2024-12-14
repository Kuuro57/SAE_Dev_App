package org.javafxapp.sae_dev_app_project.views;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.subjects.Subject;

import java.util.ArrayList;

/**
 * Classe qui représente la vue qui contient toutes les classes représentées graphiquements
 */
public class ViewAllClasses extends Pane implements Observer {

    // Attributs
    private ArrayList<ModelClass> allClassesList;



    /**
     * Constructeur de la classe
     */
    public ViewAllClasses() {
        this.allClassesList = new ArrayList<>();
    }



    /**
     * Méthode qui permet de donner un nouvel id à une classe
     * @return Le nouvel id
     */
    private int getNewId() {

        int lastId = -1;

        for (ModelClass m : this.allClassesList) {
            if (m.getId() > lastId) lastId = m.getId();
        }

        return lastId + 1;

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
            m.setId(this.getNewId());
            this.allClassesList.add(m);
            return true;

        }

        // Sinon on retourne false
        return false;
    }


    @Override
    public void update(Subject s) {

        // On initialise les coordonnées de la vbox à (0, 0)
        int coo_x = 0;
        int coo_y = 0;

        // On boucle sur la liste des classes
        for (ModelClass m : this.allClassesList) {
            // On récupère l'affichage de la classe
            VBox display = m.getDisplay();
            // On lui donnes les coordonnées
            display.setLayoutX(coo_x);
            display.setLayoutY(coo_y);
            // On l'ajoute sur le Pane
            this.getChildren().add(display);

            // On incrémente les coordoonées pour éviter les chevauchements
            coo_x += 100;
            coo_y += 50;
        }
    }


}

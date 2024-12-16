package org.javafxapp.sae_dev_app_project.subjects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.views.Observer;

import java.util.ArrayList;

/**
 * Classe qui représente le modèle d'une classe
 */
public class ModelClass implements Subject {

    // Attributs
    private int id; // Id de la classe
    private String name; // Nom de la classe
    private ArrayList<Observer> observerList;


    /**
     * Constructeur qui prend en paramètre le nom de la classe
     * @param n Nom de la classe
     */
    public ModelClass(String n) {
        this.id = -1; // Initialisation de l'id à -1 pour indiquer que le model n'a pas encore d'id
        this.name = n;
        this.observerList = new ArrayList<>();
    }


    /**
     * Méthode qui créé l'affichage de la classe
     * @return Objet de type VBox représentant une classe graphiquement
     */
    public VBox getDisplay() {

        VBox v = new VBox();
        v.setAlignment(Pos.TOP_CENTER);
        v.setPadding(new Insets(3, 3, 3,3));
        v.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        v.setMinSize(100, 100);
        v.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
        v.getChildren().add(new Text(this.name));

        return v;

    }


    @Override
    public void addObserver(Observer o) {
        if (!this.observerList.contains(o)) {
            this.observerList.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        this.observerList.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : this.observerList) {
            o.update(this);
        }
    }


    // ### GETTER / SETTER ###
    public int getId() { return id; }
    public void setId(int i) { this.id = i; }


}

package org.javafxapp.sae_dev_app_project.subjects;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.views.Observer;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.util.ArrayList;

/**
 * Classe qui représente le modèle d'une classe
 */
public class ModelClass implements Subject {

    // Attributs
    private static int LAST_ID = -1; // Nombre de classes sur le diagramme (qui représente aussi le plus grand ID)
    private int id;
    private String name; // Nom de la classe
    private int x; // Coordonnée x de l'affichage de la classe sur le diagramme
    private int y; // Coordonnée y de l'affichage de la classe sur le diagramme
    private ArrayList<Observer> observerList; // Liste qui contient tous les observeurs liés au modèle
    private ArrayList<Attribute> attributes; // Liste des attribues de la classe
    private ArrayList<Method> methods; // Liste des méthodes de la classe
    private ArrayList<Constructor> constructors; // Liste des constructeurs de la classe
    private ArrayList<ModelClass> inheritedClasses; // Liste des classes implémentées par cette classe
    private ModelClass extendedClass; // Classe qui étend cette classe, null sinon
    private boolean isSelected = false;



    /**
     * Constructeur qui prend en paramètre le nom de la classe
     * @param n Nom de la classe
     */
    public ModelClass(String n) {
        this.id = -1; // Initialisation de l'id à -1 pour indiquer que le model n'a pas encore d'id
        this.name = n;
        this.x = 0;
        this.y = 0;
        this.observerList = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.inheritedClasses = new ArrayList<>();
        this.extendedClass = null;
        this.constructors = new ArrayList<>();
    }



    /**
     * Méthode qui permet de donner un nouvel id à une classe
     * @return Le nouvel id
     */
    public static int getNewId() {
        ModelClass.LAST_ID += 1;
        return ModelClass.LAST_ID;
    }



    /**
     * Méthode qui créé l'affichage de la classe
     * @return Objet de type VBox représentant une classe graphiquement
     */
    public VBox getDisplay() {

        // Initialisation de la VBox et de son visuel
        VBox v = new VBox();
        v.setId(String.valueOf(this.id));
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
            o.update();
        }
    }



    /*
     * ### GETTERS / SETTER ###
     */
    public int getId() { return id; }
    public String getName() { return name; }
    public int getX() {return x;}
    public int getY() {return y;}
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }
    public ArrayList<Method> getMethods() {
        return methods;
    }
    public ArrayList<ModelClass> getInheritedClasses() {
        return inheritedClasses;
    }
    public void setInheritedClasses(ArrayList<ModelClass> l) { inheritedClasses = l; }
    public ModelClass getExtendedClass() {
        return extendedClass;
    }
    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }
    public void setId(int i) { this.id = i; }
    public void setExtendedClass(ModelClass extendedClass) {
        this.extendedClass = extendedClass;
    }

    public boolean isSelected() { return isSelected; }
    public void toogleIsSelected() { this.isSelected = !this.isSelected; }

    /**
     * Méthode toString qui affiche toutes les informations de la classe
     * @return String qui contient toutes les informations de la classe
     */
    public String toString(){
        String str = "Nom : " + this.name + "\n";
        str += "Id : " + this.id + "\n";
        str += "Attributs : \n";
        // On boucle sur les attributs si il y en a
        if (this.attributes != null) {
            for (Attribute a : this.attributes) {
                str += a.toString() + "\n";
            }
        }
        // On boucle sur les méthodes si il y en a
        if (this.methods != null) {
            str += "Méthodes : \n";
            for (Method m : this.methods) {
                str += m.toString() + "\n";
            }
        }
        // On boucle sur les classes mères si il y en a
        if (this.inheritedClasses != null) {
            str += "Implémentations : \n";
            for (ModelClass m : this.inheritedClasses) {
                str += m.getName() + "\n";
            }
        }
        // On affiche la classe mère si il y en a une
        if (this.extendedClass != null) {
            str += "Classe mère : " + this.extendedClass.getName() + "\n";
        }
        return str;

    }


}

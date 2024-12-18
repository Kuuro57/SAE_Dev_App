package org.javafxapp.sae_dev_app_project.subjects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.views.Observer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Classe qui représente le modèle d'une classe
 */
public class ModelClass implements Subject {

    // Attributs
    private int id; // Id de la classe
    private String name; // Nom de la classe
    private ArrayList<Observer> observerList;
    // Attributs supplémentaires Itération 2
    private ArrayList<Attribute> attributes;
    private ArrayList<Method> methods;
    private ArrayList<ModelClass> inheritedClasses;

    public void setExtendedClass(ModelClass extendedClass) {
        this.extendedClass = extendedClass;
    }

    private ModelClass extendedClass;

    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public ArrayList<ModelClass> getInheritedClasses() {
        return inheritedClasses;
    }

    public ModelClass getExtendedClass() {
        return extendedClass;
    }

    public ArrayList<Constructor> getConstructors() {
        return constructors;
    }

    private ArrayList<Constructor> constructors;


    /**
     * Constructeur qui prend en paramètre le nom de la classe
     * @param n Nom de la classe
     */
    public ModelClass(String n) {
        this.id = -1; // Initialisation de l'id à -1 pour indiquer que le model n'a pas encore d'id
        this.name = n;
        this.observerList = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.inheritedClasses = new ArrayList<>();
        this.extendedClass = null;
        this.constructors = new ArrayList<>();

    }

    /**
     * Constructeur qui prend en paramètre le nom de la classe et tous les attributs de la classe
     * @param n Nom de la classe
     * @param e Classe mère
     *
     */

    public ModelClass(String n, ModelClass e) {
        this.id = -1; // Initialisation de l'id à -1 pour indiquer que le model n'a pas encore d'id
        this.name = n;
        this.observerList = new ArrayList<>();
        this.attributes = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.inheritedClasses = new ArrayList<>();
        this.extendedClass = e;
        this.constructors = new ArrayList<>();



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

        for (Attribute a : this.attributes) {
            v.getChildren().add(a.getDisplay());
        }

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

    public String getName() { return name; }


    /*
    * Méthode toString qui affiche toutes les informations de la classe
    * @return String qui contient toutes les informations de la classe
    */

    public String toString(){
        String str = "Nom de la classe : " + this.name + "\n";
        str += "Id de la classe : " + this.id + "\n";
        str += "Attributs de la classe : \n";
        // On boucle sur les attributs si il y en a
        if (this.attributes != null) {
            for (Attribute a : this.attributes) {
                str += a.toString() + "\n";
            }
        }
        // On boucle sur les méthodes si il y en a
        if (this.methods != null) {
            str += "Méthodes de la classe : \n";
            for (Method m : this.methods) {
                str += m.toString() + "\n";
            }
        }
        // On boucle sur les classes mères si il y en a
        if (this.inheritedClasses != null) {
            str += "Classes mères de la classe : \n";
            for (ModelClass m : this.inheritedClasses) {
                str += m.getName() + "\n";
            }
        }
        // On affiche la classe mère si il y en a une
        if (this.extendedClass != null) {
            str += "Classe mère de la classe : " + this.extendedClass.getName() + "\n";
        }
        return str;
    }

    /**
     * Méthode qui retourne le bon caractère pour représenter l'accès en UML (+, -, #, {abstract})
     * @param access Il represente le type d'accès
     * @return Le bon caractère représentant l'accès
     */
    public static String convertModifier(String access) {
        StringBuffer res = new StringBuffer();

        // Si l'attribut (ou la méthode) est private
        if (access.contains("private")) {
            // On commence par "- "
            res.append("- ");
        }
        // Sinon si l'attribut (ou la méthode) est protected
        else if (access.contains("protected")) {
            // On commence par "# "
            res.append("# ");
        }
        // Sinon
        else {
            // On commence par "+ "
            res.append("+ ");
        }

        // Si l'attribut (ou la méthode) est static
        if (access.contains("static")) {
            // On ajoute "{static} "
            res.append("{static} ");
        }

        // Si la méthode est abstract
        if (access.contains("abstract")) {
            // On ajoute "{abstract} "
            res.append("{abstract} ");
        }

        // On retourne le résultat
        return res.toString();
    }

}

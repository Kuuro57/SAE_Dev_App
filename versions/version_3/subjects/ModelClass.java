package org.javafxapp.sae_dev_app_project.subjects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.importExport.SingleClassLoader;
import org.javafxapp.sae_dev_app_project.views.Observer;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

/**
 * Classe qui représente le modèle d'une classe
 */
public class ModelClass implements Subject {

    // Attributs
    private static int LAST_ID = -1; // Nombre de classes sur le diagramme (qui représente aussi le plus grand ID)
    private int id; // Id de la classe
    private String name; // Nom de la classe
    private int x; // Coordonnée x de l'affichage de la classe sur le diagramme
    private int y; // Coordonnée y de l'affichage de la classe sur le diagramme
    private ArrayList<Observer> observerList; // Liste qui contient tous les observeurs liés au modèle
    private ArrayList<Attribute> attributes; // Liste des attribues de la classe
    private ArrayList<Method> methods; // Liste des méthodes de la classe
    private ArrayList<Constructor> constructors; // Liste des constructeurs de la classe
    private ArrayList<ModelClass> inheritedClasses; // Liste des classes implémentées par cette classe
    private ModelClass extendedClass; // Classe qui étend cette classe, null sinon
    private String type; // Type de la classe


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
        this.type = "";
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

        ArrayList<String> modifs = this.hashType();
        String modifierClass;

        if (modifs.get(1) == null){
            modifierClass = modifs.get(2);
        } else {
            modifierClass = "<" + modifs.get(1) + "> " +modifs.get(2);
        }

        // Nom de la classe et Type de classe (abstract, interface, class)
        Text nomClasse = new Text(modifierClass + " " + this.name);

        // On lui donnes les coordonnées
        v.setLayoutX(this.x);
        v.setLayoutY(this.y);

        // VBOX de case classe
        v.setId(String.valueOf(this.id));
        v.setAlignment(Pos.TOP_CENTER);
        v.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1))));
        v.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, new CornerRadii(3), new Insets(0, 0, 0, 0))));
        v.getChildren().add(nomClasse);

        // VBOX des attributs
        VBox vAttributs = new VBox();
        vAttributs.setAlignment(Pos.BASELINE_LEFT);
        vAttributs.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 0))));
        vAttributs.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
        vAttributs.setPadding(new Insets(0, 3, 10, 3));

        // VBOX des méthodes
        VBox vMethods = new VBox();
        vMethods.setAlignment(Pos.BASELINE_LEFT);
        vMethods.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 0))));
        vMethods.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
        vMethods.setPadding(new Insets(0, 3, 10, 3));

        // Ajout des attributs à afficher
        for (Attribute a : this.attributes) {

            // si l'attribut n'est pas caché

            if (!a.isHidden()) {
                // si le type de l'attribut est une classe déjà chargée
                // on cache l'attribut
                for (Class<?> c : SingleClassLoader.LOADED_CLASSES) {
                    if (c.getSimpleName().equals(a.getType())) {
                        a.setHidden(true);
                    }
                }
                // sinon on affiche l'attribut
                vAttributs.getChildren().add(a.getDisplay());
            }



        }

        // Ajout des constructeurs à afficher
        for (Constructor c : this.constructors) {
            vMethods.getChildren().add(c.getDisplay());
        }

        // On ajoute les attributs à la VBox
        v.getChildren().add(vAttributs);

        // Ajout des méthodes à afficher
        for (Method m : this.methods) {
            vMethods.getChildren().add(m.getDisplay());
        }

        // On ajoute les méthodes à la VBox
        v.getChildren().add(vMethods);

        // On donne la bonne taille à la VBox
        v.autosize();

        return v;

    }

    /**
     * Méthode de traitement de la liste de paramètres d'une méthode pour changer l'affichage
     * @param listParams Liste des paramètres à traiter
     * @return L'affichage souhaité pour les paramètres des méthodes
     */
    public static String displayParams(ArrayList<Parameter> listParams){

        StringBuffer res = new StringBuffer();

        for(Parameter p : listParams){
            res.append(p.getName() + " : " + Export.removePackageName(p.getType().getTypeName()) + ", ");
        }

        Export.removeLastComa(res);
        return res.toString();

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


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }



    // Méthode qui crée l'icon correcpondant au type de classe
    private Image setIcon() {

        switch(this.type){

            case "Interface":
                return new Image("");

            case "Abstract":
                return new Image("");

            default:
                return new Image("");
        }

    }

    // Décomposition du type de classe récupéré à l'import
    public ArrayList<String> hashType(){

        String[] res = this.type.split(" ");
        ArrayList<String> list = new ArrayList<>();

        // S'il y a 3 modifier exemple:'public abstract class'
        if(res.length == 3){
            list.add(res[0]);
            list.add(res[1]);
            list.add(res[2]);
        }
        // S'il y en a 2 exemple : 'public interface'
        else if(res.length == 2){
            list.add(res[0]);
            list.add(null);
            list.add(res[1]);
        }
        // S'il n'y en a qu'un exemple : 'class'
        else if(res.length == 1){
            list.add(null);
            list.add(null);
            list.add(res[0]);
        }

        return list;
    }


}
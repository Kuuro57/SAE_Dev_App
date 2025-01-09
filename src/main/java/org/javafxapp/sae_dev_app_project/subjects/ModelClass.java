package org.javafxapp.sae_dev_app_project.subjects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.views.Observer;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

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
    private boolean isVisible; // Booléen qui permet de savoir si la classe doit être visible sur le diagramme ou non



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
        this.isVisible = true;
    }



    /**
     * Constructeur de la classe
     * @param n Nom de la classe
     * @param atts Liste des attributs de la classe
     * @param meths Liste des méthodes de la classe
     * @param consts Liste des constructeurs de la classe
     * @param tpe Type de la classe
     */
    public ModelClass(String n, ArrayList<Attribute> atts, ArrayList<Method> meths, ArrayList<Constructor> consts, String tpe) {
        this.id = -1; // Initialisation de l'id à -1 pour indiquer que le model n'a pas encore d'id
        this.name = n;
        this.x = 0;
        this.y = 0;
        this.observerList = new ArrayList<>();
        this.attributes = atts;
        this.methods = meths;
        this.inheritedClasses = new ArrayList<>();
        this.extendedClass = null;
        this.constructors = consts;
        this.type = tpe;
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
    public VBox getDisplay(ViewAllClasses viewAllClasses) {

        // Initialisation de la VBox et de son visuel
        VBox v = new VBox();
        HBox hb = new HBox();
        hb.setSpacing(5);
        hb.setAlignment(Pos.CENTER);


        // Nom de la classe et Type de classe (abstract, interface, class)
        Text nomClasse = new Text(this.name);
        nomClasse.setFont(Font.font(14));
        hb.getChildren().addAll(setIcon(), nomClasse);

        // On lui donnes les coordonnées
        v.setLayoutX(this.x);
        v.setLayoutY(this.y);

        // VBOX de case classe
        v.setId(String.valueOf(this.id));
        v.setAlignment(Pos.TOP_CENTER);
        v.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1,1,0,1))));
        v.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, new CornerRadii(3), new Insets(0))));
        v.getChildren().add(hb);


        // VBOX des attributs
        VBox vAttributs = new VBox();
        vAttributs.setAlignment(Pos.BASELINE_LEFT);
        vAttributs.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 0, 0))));
        vAttributs.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
        vAttributs.setPadding(new Insets(0, 3, 10, 3));

        // VBOX des méthodes
        VBox vMethods = new VBox();
        vMethods.setAlignment(Pos.BASELINE_LEFT);
        vMethods.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(1, 0, 1, 0))));
        vMethods.setBackground(new Background(new BackgroundFill(Color.WHITE, null, new Insets(0, 0, 0, 0))));
        vMethods.setPadding(new Insets(0, 3, 10, 3));


        // Ajout des attributs à afficher
        for (Attribute a : this.attributes) {

            // Si l'attribut n'est pas caché
            if (!a.isHidden()) {
                // Si le nom de l'attribut est le même que le nom d'une classe affichée sur le diagramme
                for (ModelClass m : viewAllClasses.getAllClasses()) {
                    if (m.getName().equals(a.getType()) && m.isVisible()) {
                        // On cache l'attribut
                        a.setHidden(true);
                    }
                    // Si l'attribut est une collection simple
                    else if (a.getType().matches(".*<.*>")) {
                        String[] type = a.getType().split("<");
                        String[] type2 = type[1].split(">");
                        // On regarde si le type entre <> est le même que le nom d'une classe affichée sur le diagramme
                        for (ModelClass m2 : viewAllClasses.getAllClasses()) {
                            if (m2.getName().equals(type2[0]) && m2.isVisible()) {
                                // On cache l'attribut
                                a.setHidden(true);
                            }
                        }
                    }
                }
            }

            // Si l'attribut est de type collection et que la classe dont il vient n'est pas visible
            // On passe l'attribut en visible
            if (a.getType().matches(".*<.*>")) {
                String[] type = a.getType().split("<");
                String[] type2 = type[1].split(">");
                for (ModelClass m2 : viewAllClasses.getAllClasses()) {
                    if (m2.name.equals(type2[0]) && !m2.isVisible) {
                        a.setHidden(false);
                    }
                }
            }

        }



        // Ajout des attributs (seulement si ils ne sont pas masqués)
        for (Attribute a : this.attributes) {
            if (!a.isHidden()) vAttributs.getChildren().add(a.getDisplay());
        }

        // Ajout des constructeurs (seulement si ils ne sont pas masqués)
        for (Constructor c : this.constructors) {
            if (!c.isHidden()) vMethods.getChildren().add(c.getDisplay());
        }

        // Ajout des méthodes (seulement si elles ne sont pas masquées)
        for (Method m : this.methods) {
            if (!m.isHidden()) vMethods.getChildren().add(m.getDisplay());
        }



        // On ajoute les méthodes à la VBox
        v.getChildren().addAll(vAttributs, vMethods);

        // On donne la bonne taille à la VBox
        v.autosize();

        // On retourne la VBox
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
            res.append(p.getName() + " : " + Export.removePackageName(p.getType()) + ", ");
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
     * ### GETTERS ###
     */
    public int getId() { return id; }
    public String getName() { return name; }
    public int getX() {return x;}
    public int getY() {return y;}
    public ArrayList<Attribute> getAttributes() { return attributes; }
    public ArrayList<Method> getMethods() { return methods; }
    public ArrayList<ModelClass> getInheritedClasses() { return inheritedClasses; }
    public ModelClass getExtendedClass() { return extendedClass; }
    public ArrayList<Constructor> getConstructors() { return constructors; }
    public boolean isVisible() { return this.isVisible; }
    public String getType() { return type; }



    /*
     * ### GETTERS ###
     */
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setId(int i) { this.id = i; }
    public void setName(String name) { this.name = name; }
    public void setExtendedClass(ModelClass extendedClass) { this.extendedClass = extendedClass; }
    public void setVisibility(boolean b) { this.isVisible = b; }
    public void setType(String type) { this.type = type; }



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



    /**
     * Méthode qui crée l'icon correcpondant au type de classe
     * @return Objet de type Pane correspondant à l'affichage de l'icon
     */
    private Pane setIcon() {

        StackPane pane = new StackPane();
        Circle icon = new Circle();
        icon.setRadius(10);
        icon.setStrokeWidth(1.4);

        Label label = new Label();
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        if (type.contains("interface")) {
            icon.setFill(Color.MEDIUMSEAGREEN);
            icon.setStroke(Color.DARKGREEN);
            label.setText("I");
            label.setTextFill(Color.DARKGREEN);
            pane.getChildren().addAll(icon, label);

        } else if (type.contains("abstract")) {
            icon.setFill(Color.rgb(255, 90, 94));
            icon.setStroke(Color.DARKRED);
            icon.getStrokeDashArray().setAll(4.0, 4.0);
            label.setText("A");
            label.setTextFill(Color.DARKRED);
            pane.getChildren().addAll(icon, label);

        } else {
            icon.setFill(Color.DODGERBLUE);
            icon.setStroke(Color.BLUE);
            label.setText("C");
            label.setTextFill(Color.BLUE);
            pane.getChildren().addAll(icon, label);

        }
        return pane;

    }



    /**
     * Méthode qui décompose le type de la classe récupéré à l'import
     * @return Une liste contenant les différents type
     */
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



    /**
     * Méthode qui cache tous les attributs de la classe
     */
    public void hideAllAttributes() {
        for (Attribute a : attributes) a.setHidden(true);
    }



    /**
     * Méthode qui affiche tous les attributs de la classe
     */
    public void showAllAttributes() {
        for (Attribute a : attributes) a.setHidden(false);
    }



    /**
     * Méthode qui cache toutes les méthodes de la classe
     */
    public void hideAllMethods() {
        for (Method m : methods) m.setHidden(true);
    }



    /**
     * Méthode qui affiche toutes les méthodes de la classe
     */
    public void showAllMethods() {
        for (Method m : methods) m.setHidden(false);
    }



    /**
     * Méthode qui cache tous les constructeurs de la classe
     */
    public void hideConstructors() {
        for (Constructor c : constructors) c.setHidden(true);
    }



    /**
     * Méthode qui affiche tous les constructeurs de la classe
     */
    public void showConstructors() {
        for (Constructor c : constructors) c.setHidden(false);
    }



    /**
     * Méthode qui cache tous les attributs, méthodes et constructeurs de la classe
     */
    public void hideDetails() {
        this.hideAllAttributes();
        this.hideAllMethods();
        this.hideConstructors();
    }


}

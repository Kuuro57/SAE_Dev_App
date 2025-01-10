package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.util.ArrayList;

/**
 * Classe qui représente une méthode d'une classe
 */
public class Method extends ClassComponent {

    // Attributs
    private String returnType; // Type de retour de la méthode
    private ArrayList<Parameter> parameters; // Liste des paramètres de la méthode



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès de la méthode
     * @param name Nom de la méthode
     * @param parameters Liste des paramètres de la méthode
     * @param returnType Type de retour de la méthode
     */
    public Method(String modifier, String name, ArrayList<Parameter> parameters, String returnType) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.hidden = false; // Visible par défaut
    }

    public Method(String modifier, String name, String returnType) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
        this.hidden = false;
    }


    /*
     * ### GETTERS ###
     */
    public String getReturnType() { return returnType; }
    public ArrayList<Parameter> getParameters() { return parameters; }



    @Override
    public HBox getDisplay() {
        // Initialisation de la HBox
        HBox hBox = new HBox();
        String parametres = "";

        // Si la méthode requiert des paramètres, on récupère ses paramètres
        if (!this.getParameters().isEmpty()) parametres = ModelClass.displayParams(parameters);

        // On créé l'affichage de la méthode
        Text nomMethod = new Text(Export.convertModifier(modifier) + name + "(" + parametres + ") : " + Export.removePackageName(returnType));

        // On l'ajoute à la HBox et on la renvoie
        hBox.getChildren().add(nomMethod);
        return hBox;
    }



    @Override
    public String toString() {
        return modifier + " " + returnType + " " + name + "(" + parameters.toString() + ")";
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

}
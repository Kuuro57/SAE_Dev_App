package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.importExport.Export;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.util.ArrayList;


/**
 * Classe qui représente un constructeur d'une classe
 */
public class Constructor extends ClassComponent {

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    // Attributs
    private ArrayList<Parameter> parameters; // Liste des paramètres du constructeur



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès du constructeur
     * @param name Nom du constructeur
     */

    public Constructor(String modifier, String name) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = new ArrayList<>();
        this.hidden = false;
    }



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès du constructeur
     * @param name Nom du constructeur
     * @param parameters Liste des paramètres du constructeur
     */
    public Constructor(String modifier, String name, ArrayList<Parameter> parameters) {
        this.modifier = modifier;
        this.name = name;
        this.parameters = parameters;
        this.hidden = false;
    }



    @Override
    public HBox getDisplay() {
        HBox hBox = new HBox();
        String parametres = "";

        // Si la méthode requiert des paramètres
        if (!this.getParameters().isEmpty()) {

            parametres = ModelClass.displayParams(parameters);

        }

        // On affiche la méthode sur le diagramme de classe
        Text nomConstructeur = new Text(Export.convertModifier(modifier) + name + "(" + parametres + ")");

        hBox.getChildren().add(nomConstructeur);

        return hBox;
    }



    @Override
    public String toString() {
        return modifier + " " + name;
    }


    public void addParameter(Parameter parameter) {
        parameters.add(parameter);
    }

}

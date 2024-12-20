package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.importExport.FileManipulator;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.lang.reflect.Parameter;
import java.util.ArrayList;

public class Method extends ClassComponent {
    private String returnType;
    private ArrayList<Parameter> parameters;

    public Method(String modifier, String name, ArrayList<Parameter> parameters, String returnType) {
        this.modifier = modifier;
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
    }

    public String getReturnType() {
        return returnType;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
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
        Text nomMethod = new Text(FileManipulator.convertModifier(modifier) + name + "(" + parametres + ") : " + FileManipulator.removePackageName(returnType));

        hBox.getChildren().add(nomMethod);

        return hBox;

    }

    @Override
    public String toString() {
        return modifier + " " + returnType + " " + name + "(" + parameters.toString() + ")";
    }

}
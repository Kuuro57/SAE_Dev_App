package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import java.awt.*;
import java.lang.reflect.Modifier;


/**
 * Class qui représente un attribut d'une classe
 */
public class Attribute extends ClassComponent {

    // Attributs
    private String type; // Type de l'attribut



    /**
     * Constructeur de la classe
     * @param modifier Type d'accès à l'attribut
     * @param name Nom de l'attribut
     * @param type Type de l'attribut
     */
    public Attribute(String modifier, String name, String type) {
        this.modifier = modifier;
        this.name = name;
        this.type = type;
    }



    @Override
    public HBox getDisplay() {
        // Création de la HBox
        HBox hBox = new HBox();
        hBox.setSpacing(10);

        // Ajout du type d'accès
        Text modifierText = new Text(ModelClass.convertModifier(modifier));
        hBox.getChildren().add(modifierText);

        // Ajout du nom
        Text nameText = new Text(name);
        hBox.getChildren().add(nameText);

        // Ajout du type
        Text typeText = new Text(" : " + type);
        hBox.getChildren().add(typeText);

        return hBox;


    }



    /*
     * ### GETTERS ###
     */
    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        // modifier depuis ModelClass
        return ModelClass.convertModifier(modifier) + " " + name + " : " + type;
    }

}

package org.javafxapp.sae_dev_app_project.classComponent;

import javafx.scene.layout.HBox;


/**
 * Classe qui représente les attributs communs des composants d'une classe
 */
public abstract class ClassComponent {

    // Attributs
    protected String modifier; // Type d'accès (public, private, protected, abstract, static)
    protected String name; // Nom du composant
    protected boolean hidden; // Si le composant est caché ou non



    /**
     * Méthode qui permet de créé l'affichage graphique du composant
     * @return Une HBox représentant graphiquement l'affichage du composant
     */
    public abstract HBox getDisplay();



    /*
     * ### GETTERS ###
     */
    public String getName() { return name; }
    public String getModifier() { return modifier; }
    public boolean isHidden() {
        return hidden;
    }



    /*
     * ### SETTERS ###
     */
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }


}

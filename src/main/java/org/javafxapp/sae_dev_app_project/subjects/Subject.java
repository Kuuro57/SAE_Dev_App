package org.javafxapp.sae_dev_app_project.subjects;

import org.javafxapp.sae_dev_app_project.views.Observer;

/**
 * Interface qui représente un sujet
 */
public interface Subject {


    /**
     * Méthode qui ajoute un observeur au sujet
     * @param o Observeur que l'on veut ajouter
     */
    public void addObserver(Observer o);



    /**
     * Méthode qui supprime un observeur du sujet
     * @param o Observeur que l'on veut supprimer
     */
    public void removeObserver(Observer o);



    /**
     * Méthode qui notifie tout les observeurs que le sujet à changer
     */
    public void notifyObservers();


}

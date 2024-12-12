package org.javafxapp.sae_dev_app_project.views;

import org.javafxapp.sae_dev_app_project.subjects.Subject;

/**
 * Classe qui représente un observeur
 */
public interface Observer {


    /**
     * Méthode qui met à jour l'observeur
     * @param s Sujet sur lequel l'observeur va puiser les données pour ce mettre à jour
     */
    public void update(Subject s);

}

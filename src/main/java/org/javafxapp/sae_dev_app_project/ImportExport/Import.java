package org.javafxapp.sae_dev_app_project.ImportExport;

import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import static org.javafxapp.sae_dev_app_project.ImportExport.FileManipulator.hasBeenLoaded;

public class Import {




    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Les informations de la classe
     */
    public static ModelClass getModelClass(String nomClasse) {

        try {



            Class<?> classe = null;
            if (hasBeenLoaded(nomClasse) != null){
                classe = hasBeenLoaded(nomClasse);
            }
            else{
                classe = Class.forName(nomClasse);
            }

        }
        catch (ClassNotFoundException e) {
            System.out.println("La classe n'a pas été trouvée");
        }

        // Modele de la classe renvoyée

        ModelClass model = new ModelClass(nomClasse);
        return model;

    }




}
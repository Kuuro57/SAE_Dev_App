package org.javafxapp.sae_dev_app_project.ImportExport;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileManipulator {

    /*
     * Méthode qui vérifie si une classe a déjà été chargée
     * @param nomClasse Nom de la classe à vérifier
     * @return La classe si elle a déjà été chargée, null sinon
     * */

    public static Class<?> hasBeenLoaded(String nomClasse){
        Class<?> loadedClass = null;
        Set<Class<?>> loadedClasses = CustomClassLoader.getLoadedClasses();
        // conversion du set en liste
        List<Class<?>> list = new ArrayList<>(loadedClasses);
        // parcours de la liste
        for (Class<?> cl:  list) {
            // si la classe est déjà chargée
            if (cl.getName().equals(nomClasse)){
                // on retourne la classe
                loadedClass = cl;
            }

        }

        return loadedClass;

    }



}

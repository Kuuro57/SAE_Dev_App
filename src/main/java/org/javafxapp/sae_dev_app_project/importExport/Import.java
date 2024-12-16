package org.javafxapp.sae_dev_app_project.importExport;

import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.File;


public class Import {


    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Les informations de la classe
     */
    public static ModelClass getModelClass(String nomClasse) {
        // Modele de la classe renvoyée
        return new ModelClass(nomClasse);
    }


    /**
     * Méthode qui importe la classe dans le diagramme des classes
     * @param view Vue qui comprend toutes les classes
     * @return True si l'import s'est bien déroulé, false sinon
     * @throws ClassNotFoundException Cas où la classe n'est pas trouvée
     */
    public static boolean importClass(ViewAllClasses view) throws ClassNotFoundException {

        // On demande à l'utilisateur de choisir un fichier .class
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openFileChooser();

        // Si un fichier a été choisi
        if (file != null) {

            // On récupère le chemin du fichier et le nom de la classe
            String classPath = file.getParent();
            String className = file.getName().replace(".class", "");

            // On charge la classe
            CustomClassLoader customClassLoader = new CustomClassLoader(classPath);

            // On charge la classe
            Class<?> loadedClass = customClassLoader.loadClass(className);

            // On récupère le nom de la classe, on crée un modèle et on l'ajoute à la vue graphique
            ModelClass model = Import.getModelClass(loadedClass.getSimpleName());
            model.addObserver(view);
            view.addClass(model);

            // On retourne true
            return true;

        }

        // Sinon si le fichier est null
        else {
            // On retourne false
            return false;
        }

    }




}
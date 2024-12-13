package org.javafxapp.sae_dev_app_project.importExport;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.File;

/**
 * Classe qui gère le choix de fichier
 */
public class FileChooserHandler {

    // attribut qui représente la vue graphique
    private ViewAllClasses graphicView;

    /**
     * Constructeur de la classe
     *
     * @param graphicView Vue graphique
     */
    public FileChooserHandler(ViewAllClasses graphicView) {
        this.graphicView = graphicView;
    }

    /**
     * Méthode qui ouvre un FileChooser pour choisir un fichier .class
     *
     * @param stage Stage de l'application
     */
    public void openFileChooser(Stage stage) {
        // Création du FileChooser
        FileChooser fileChooser = new FileChooser();
        // Ajout d'un filtre pour les fichiers .class
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Class", "*.class"));
        // Titre de la fenêtre
        fileChooser.setTitle("Choisir un fichier .class");
        // Affichage de la fenêtre
        File file = fileChooser.showOpenDialog(stage);

        //si un fichier a été choisi, on charge la classe
        if (file != null) {
            // On récupère le chemin du fichier et le nom de la classe
            String classPath = file.getParent();
            String className = file.getName().replace(".class", "");

            // On charge la classe
            CustomClassLoader customClassLoader = new CustomClassLoader(classPath);
            try {
                //ici on charge la classe et on l'ajoute à la vue graphique, utilisation de la logique de chargement de classe dans le projet
                Class<?> loadedClass = customClassLoader.loadClass(className);
                // On récupère le nom de la classe, on crée un modèle et on l'ajoute à la vue graphique
                ModelClass model = Import.getModelClass(loadedClass.getSimpleName());
                model.addObserver(graphicView);
                graphicView.addClass(model);
                model.notifyObservers();

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

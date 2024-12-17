package org.javafxapp.sae_dev_app_project.importExport;

import javafx.stage.FileChooser;

import java.io.File;

/**
 * Classe qui gère le choix de fichier
 */
public class FileChooserHandler {


    /**
     * Constructeur de la classe
     */
    public FileChooserHandler() {
    }



    /**
     * Méthode qui ouvre un FileChooser pour choisir un fichier .class
     * @return Objet de type
     */
    public File openFileChooser() {
        // Création du FileChooser
        FileChooser fileChooser = new FileChooser();
        // Ajout d'un filtre pour les fichiers .class
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Class", "*.class"));
        // Titre de la fenêtre
        fileChooser.setTitle("Choisir un fichier .class");
        // Affichage de la fenêtre
        return fileChooser.showOpenDialog(null);

    }



    /**
     * Méthode qui ouvre un pop-up permettant de choisir un dossier et un nom de fichier
     * @return Un objet de type File comprenant le chemin et le nom du fichier à enregistrer
     */
    public File openRepositoryPathAndFileNameChooser(String exportType) {

        // Initialisation des variables utiles à la création du FileChooser
        FileChooser fileChooser = new FileChooser();
        String title;
        String fileType;
        String extension;
        String defaultFileName;

        // Switch des types d'exports souhaités assignés à un code spécifique
        switch (exportType) {

            // Cas où on exporte en Image PNG
            case "png":
                title = "Enregistrer le fichier PNG";
                fileType = "Fichier PNG";
                extension = "*.png";
                defaultFileName = "image.png";
                break;

            // Cas où on exporte en code plantUml
            case "codepuml":
                title = "Enregistrer le fichier TXT";
                fileType = "Fichier TXT";
                extension = "*.txt";
                defaultFileName = "code.txt";
                break;

            default:
                return null;
        }

        // On initialise les informations du FileChooser
        fileChooser.setTitle(title);
        FileChooser.ExtensionFilter pumlFilter = new FileChooser.ExtensionFilter(fileType, extension);
        fileChooser.getExtensionFilters().add(pumlFilter);
        fileChooser.setInitialFileName(defaultFileName);

        // Affichage du FileChooser
        return fileChooser.showSaveDialog(null);

    }

}

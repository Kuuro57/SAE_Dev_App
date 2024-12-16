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
     * @return Une string représentant le chemin du dossier sélectionné
     */
    public File openRepositoryPathAndFileNameChooser() {

        // Initialisation du FileChooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le fichier PNG");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Fichiers PNG", "*.png");
        fileChooser.getExtensionFilters().add(pngFilter);
        fileChooser.setInitialFileName("image.png");

        // Affichage du FileChooser
        return fileChooser.showSaveDialog(null);

    }



}

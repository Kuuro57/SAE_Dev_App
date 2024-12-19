package org.javafxapp.sae_dev_app_project.importExport;

import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;


public class Import {


    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Les informations de la classe
     */
    public static ModelClass getModelClass(ViewAllClasses view, String nomClasse) {

        // Initialisation du potentiel même id
        int sameId = -1;

        // Si la classe est déjà chargée
        if (SingleClassLoader.hasBeenLoaded(nomClasse)) {
            // On récupère l'id de la classe déjà chargée
            for (ModelClass m : view.getAllClasses()) {
                if (m.getName().equals(nomClasse)) {
                    sameId = m.getId();
                    break;
                }
            }
        }


        // On récupère l'objet Class depuis le nom de la classe
        Class<?> clas = SingleClassLoader.getClassForName(nomClasse);

        // Si la classe n'existe pas
        if (clas == null) {
            // On retourne null
            return null;
        }

        // On initialise le modèle de la classe et on lui donne un nouvel ID
        ModelClass model = new ModelClass(clas.getSimpleName());


        if (sameId != -1) {
            model.setId(sameId);
        }
        else {
            model.setId(ModelClass.getNewId());
        }




        // Si la classe hérite d'une classe
        if (clas.getSuperclass() != null) {
            // Si cette classe est déjà chargée
            if (SingleClassLoader.hasBeenLoaded(clas.getSuperclass().getSimpleName())) {
                // On ajoute à la classe la classe héritée
                model.setExtendedClass(Import.getModelClass(view, clas.getSuperclass().getSimpleName()));
            }
        }


        // On boucle sur les implémentations de la classe
        ArrayList<ModelClass> newListInterface = new ArrayList<>();
        for (Class<?> c : clas.getInterfaces()) {
            // Si la classe est déjà chargée
            if (SingleClassLoader.hasBeenLoaded(c.getSimpleName())) {
                // On ajoute à la liste des implémentations du modèle la classe
                newListInterface.add(Import.getModelClass(view, c.getSimpleName()));
            }
        }
        // On ajoute au modèle les implémentations
        model.setInheritedClasses(newListInterface);



        // On retourne le model de la classe
        return model;

    }



    /**
     * Méthode qui importe la classe dans le diagramme des classes
     * @param view Vue qui comprend toutes les classes
     * @throws FileNotFoundException Cas où la classe n'est pas trouvée
     */
    public static void importClass(ViewAllClasses view) throws Exception {


        // On demande à l'utilisateur de choisir un fichier .class
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openFileChooser();

        // Si un fichier a été choisi
        if (file != null) {

            File rootPath = new File(file.getParent());

            // On charge la classe
            SingleClassLoader singleClassLoader = new SingleClassLoader();
            Class<?> clas = singleClassLoader.loadClassFromFile(file, rootPath);

            // On créé le modèle et on l'ajoute à la vue graphique
            ModelClass model = Import.getModelClass(view, clas.getSimpleName());
            model.addObserver(view);
            view.addClass(model);

        }

        else {
            throw new FileNotFoundException("Fichier non choisi");
        }



    }



    /**
     * Méthode qui importe toutes les classes d'un package
     * @param view Vue qui comprend toutes les classes
     */
    public static void importPackage(ViewAllClasses view) throws Exception {


        // On demande à l'utilisateur de choisir un dossier où ce trouve les fichiers .class
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openPackageChooser();

        // Si un dossier a été choisi
        if (file != null) {

            // On récupère la liste des fichiers dans le dossier en ne gardant que les fichiers .class en regardant l'extension
            File[] files = file.listFiles((dir, name) -> name.endsWith(".class"));
            File rootPath = file.getAbsoluteFile();

            System.out.println(Arrays.toString(files));
            System.out.println(rootPath.getAbsolutePath());
            System.out.println(files[0].getAbsolutePath());
            System.out.println("-------------");

            // Initialisation du ClassLoader
            SingleClassLoader singleClassLoader = new SingleClassLoader();

            // Pour chaque fichier on importe la classe
            for (File f : files) {

                // On charge la classe avec le CustomClassLoader
                Class<?> clas = singleClassLoader.loadClassFromFile(f, rootPath);

                // On récupère le nom de la classe, on crée un modèle et on l'ajoute à la vue graphique
                ModelClass model = Import.getModelClass(view, clas.getSimpleName());
                model.addObserver(view);
                view.addClass(model);

            }

        }
        else {
            throw new FileNotFoundException("Dossier non choisi");
        }

    }




}
package org.javafxapp.sae_dev_app_project.importExport;

import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

import static org.javafxapp.sae_dev_app_project.importExport.FileManipulator.hasBeenLoaded;


public class Import {


    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Les informations de la classe
     */
    public static ModelClass getModelClass(String nomClasse, String path) {
        // modele de la classe à retourner
        ModelClass modelClasse = new ModelClass(nomClasse);

        try {
            Class<?> classe = null;
            if (hasBeenLoaded(nomClasse) != null){
                classe = hasBeenLoaded(nomClasse);
            }
            else{
                // utilisation du custom class loader avec le path spécifié
                CustomClassLoader cl = new CustomClassLoader(path);
                classe = cl.findClass(nomClasse);
            }

            // Type de la classe (abstract, interface, class)
            int numModifClass = classe.getModifiers();
            String type = "";

            // décode le type de la classe
            switch (numModifClass) {
                case 1025:
                    type ="<<Interface>>";
                    modelClasse.setType(type);
                    break;
                case 1024:
                    type = "Class";
                    modelClasse.setType(type);
                    break;
                case 1026:
                    type =" <<Abstract>> Class";
                    modelClasse.setType(type);
                    break;
                default:
                    type = "Class";
                    modelClasse.setType(type);
                    break;
            }

            // ajout liste
            for (Class<?> c : classe.getInterfaces()) {

                // Si la classe a une classe mère, extends
                if (!(classe.getSuperclass() == null)){
                    // si la classe mère est une classe abstraite
                    if (Modifier.isAbstract(classe.getSuperclass().getModifiers())){
                        // On construit le modèle de la classe mère
                        ModelClass modelClasseMere = Import.getModelClass(classe.getSuperclass().getSimpleName(),path);
                        // On ajoute la classe mère comme classe mère de la classe
                        modelClasse.setExtendedClass(modelClasseMere);
                    }
                }
            }



            // liste des interfaces implémentées
            for (Class<?> c : classe.getInterfaces()) {
                // On construit le modèle de l'interface
                ModelClass modelInterface = Import.getModelClass(c.getSimpleName(),path);
                // On ajoute l'interface à la liste des interfaces
                modelClasse.getInheritedClasses().add(modelInterface);
            }


            // On récupère les attributs de cette classe
            for (Field att : classe.getDeclaredFields()) {
                // On ajoute l'accessibilité de l'attribut (private, protected, public)
                int numModif = att.getModifiers();
                String nomModif = Modifier.toString(numModif);
                Attribute attribute = new Attribute(nomModif, att.getGenericType().getTypeName(), att.getName());

                // On ajoute l'attribut à la liste des attributs
                modelClasse.getAttributes().add(attribute);

            }

            // On récupère le(s) constructeur(s) de cette classe
            for (java.lang.reflect.Constructor<?> constructeur : classe.getDeclaredConstructors()) {
                        // on construit l'onjet constructor
                        Constructor constructor = new Constructor(Modifier.toString(constructeur.getModifiers()), constructeur.getName());
                        // on ajoute le constructeur à la liste des constructeurs
                        modelClasse.getConstructors().add(constructor);
            }


            // On récupère les méthodes de cette classe
            for (java.lang.reflect.Method methode : classe.getDeclaredMethods()) {
                // On affiche l'accessibilité de la méthode
                int numModif = methode.getModifiers();

                String nomModif = Modifier.toString(numModif);

                ArrayList<Parameter> listParams = new ArrayList<>();

                for (Parameter p : methode.getParameters()) {

                    listParams.add(p);

                }

                // on construit l'objet method
                Method method = new Method(nomModif, methode.getName(), listParams, methode.getReturnType().getTypeName());
                // On ajoute la méthode à la liste des méthodes
                modelClasse.getMethods().add(method);

            }



        }
        catch (ClassNotFoundException e) {
            System.out.println("La classe " + nomClasse + " n'a pas été trouvée");
            e.printStackTrace();
        }


        // Modele de la classe renvoyée
        return modelClasse;
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
            ModelClass model = Import.getModelClass(loadedClass.getSimpleName(), classPath);
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



    /**
     * Méthode qui importe toutes les classes d'un package
     * @param view Vue qui comprend toutes les classes
     * @return True si l'import s'est bien déroulé, false sinon
     */
    public static boolean importPackage(ViewAllClasses view) throws ClassNotFoundException {

        // On demande à l'utilisateur de choisir un dossier où ce trouve les fichiers .class
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openPackageChooser();

        // Si un dossier a été choisi
        if (file != null) {

            // On récupère la liste des fichiers dans le dossier
            File[] files = file.listFiles();
            // ne garde que les fichiers .class en regardant l'extension
            files = file.listFiles((dir, name) -> name.endsWith(".class"));

            // Pour chaque fichier on importe la classe
            for (File f : files) {

                // On récupère le chemin du fichier et le nom de la classe
                String classPath = f.getParent();
                String className = f.getName().replace(".class", "");

                // On charge la classe avec le CustomClassLoader
                CustomClassLoader customClassLoader = new CustomClassLoader(classPath);
                Class<?> loadedClass = customClassLoader.loadClass(className);

                // On récupère le nom de la classe, on crée un modèle et on l'ajoute à la vue graphique
                ModelClass model = Import.getModelClass(loadedClass.getSimpleName(), classPath);
                model.addObserver(view);
                view.addClass(model);

            }

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
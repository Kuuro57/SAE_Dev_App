package org.javafxapp.sae_dev_app_project.importExport;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.treeView.PackageNode;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;


/**
 * Classe qui contient toutes les méthodes concernant les imports des classes
 */
public class Import {

    // Attributs
    private static TreeView<PackageNode> treeView; // Arbre des packages



    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param view Vue qui contient toutes les classes présentes sur le diagramme
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Objet de type ModelClass qui représente la classe
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

        // On initialise le modèle de la classe
        ModelClass modelClass = new ModelClass(clas.getSimpleName());

        // On lui donne un nouvel ID ou son ancien ID
        if (sameId != -1) modelClass.setId(sameId);
        else modelClass.setId(ModelClass.getNewId());


        // Type de la classe (abstract, interface, class)
        int numModifClass = clas.getModifiers();
        String type = "";

        // décode le type de la classe
        switch (numModifClass) {
            case 1537:
                type ="public interface";
                modelClass.setType(type);
                break;
            case 1026:
                type ="private abstract class";
                modelClass.setType(type);
                break;
            case 1025:
                type ="public abstract class";
                modelClass.setType(type);
                break;
            case 1028:
                type ="protected abstract class";
                modelClass.setType(type);
                break;
            case 4:
                type ="protected class";
                modelClass.setType(type);
                break;
            case 2:
                type ="private class";
                modelClass.setType(type);
                break;
            case 1:
                type ="public class";
                modelClass.setType(type);
                break;
            default:
                type = "class";
                modelClass.setType(type);
                break;
        }

        // Si la classe hérite d'une classe
        if (clas.getSuperclass() != null) {
            // Si cette classe est déjà sur le graphique
            if (view.findClassByName(clas.getSuperclass().getSimpleName()) != null) {
                // On ajoute à la classe la classe héritée
                modelClass.setExtendedClass(Import.getModelClass(view, clas.getSuperclass().getSimpleName()));
            }
        }



        // On boucle sur les interfaces de cette classe
        for (Class<?> c : clas.getInterfaces()) {
            // Si la classe est déjà sur le modèle
            if (view.findClassByName(c.getSimpleName()) != null) {
                // On construit le modèle de l'interface
                ModelClass modelInterface = Import.getModelClass(view, c.getSimpleName());
                // On ajoute l'interface à la liste des interfaces
                modelClass.getInheritedClasses().add(modelInterface);
            }
        }



        // On récupère les attributs de cette classe
        for (Field att : clas.getDeclaredFields()) {
            // On ajoute l'accessibilité de l'attribut (private, protected, public)
            int numModif = att.getModifiers();
            String nomModif = Modifier.toString(numModif);
            Attribute attribute = new Attribute(nomModif, Export.removePackageName(att.getGenericType().getTypeName()), Export.removePackageName(att.getName()));

            // On ajoute l'attribut à la liste des attributs
            modelClass.getAttributes().add(attribute);
        }


        // On récupère le(s) constructeur(s) de cette classe
        for (java.lang.reflect.Constructor<?> constructeur : clas.getDeclaredConstructors()) {
            ArrayList<Parameter> params = new ArrayList<>();

            for (java.lang.reflect.Parameter p : constructeur.getParameters()) {
                Parameter parm = new Parameter(p.getType().getTypeName(), p.getName());
                params.add(parm);
            }

            // On construit l'objet constructor
            Constructor constructor = new Constructor(Modifier.toString(constructeur.getModifiers()), Export.removePackageName(constructeur.getName()), params);
            // On ajoute le constructeur à la liste des constructeurs
            modelClass.getConstructors().add(constructor);
        }


        // On récupère les méthodes de cette classe
        for (java.lang.reflect.Method methode : clas.getDeclaredMethods()) {
            // On affiche l'accessibilité de la méthode
            int numModif = methode.getModifiers();
            String nomModif = Modifier.toString(numModif);

            // On récupère la liste des paramètres de cette méthode
            ArrayList<Parameter> listParams = new ArrayList<>();

            for (java.lang.reflect.Parameter param : methode.getParameters()) {
                Parameter p = new Parameter(param.getType().getTypeName(), param.getName());
                listParams.add(p);
            }

            // On construit l'objet Method
            Method method = new Method(nomModif, methode.getName(), listParams, methode.getReturnType().getTypeName());
            // On ajoute la méthode à la liste des méthodes
            modelClass.getMethods().add(method);
        }

        // On retourne le model de la classe
        return modelClass;

    }



    /**
     * Méthode qui importe la classe dans le diagramme des classes
     * @param view Vue qui comprend toutes les classes
     * @throws FileNotFoundException Cas où la classe n'est pas trouvée
     */
    public static void importClass(ViewAllClasses view) throws FileNotFoundException {

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
        else throw new FileNotFoundException("Fichier non choisi");
    }



    /**
     * Méthode qui importe toutes les classes d'un package
     */
    public static void importPackage() throws FileNotFoundException {


        // On demande à l'utilisateur de choisir un dossier où ce trouve les fichiers .class
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openPackageChooser();

        // Si un dossier a été choisi
        if (file != null) {

            // On récupère la liste des fichiers dans le dossier en ne gardant que les fichiers .class en regardant l'extension
            File[] files = file.listFiles((dir, name) -> name.endsWith(".class"));
            File rootPath = file.getAbsoluteFile();

            // Si le dossier ne comprend pas de fichiers .class
            if (files.length == 0) {
                throw new FileNotFoundException("Le dossier sélectionné ne contient pas de fichier .class");
            }

            // Initialisation du ClassLoader
            SingleClassLoader singleClassLoader = new SingleClassLoader();

            // Pour chaque fichier on importe la classe
            for (File f : files) {

                // On charge la classe avec le CustomClassLoader
                Class<?> clas = singleClassLoader.loadClassFromFile(f, rootPath);

                // On ajoute la classe à l'arbre des packages
                addClassToTreeView(clas.getName(), f.getParent());
            }

        }
        else {
            throw new FileNotFoundException("Dossier non choisi");
        }

    }



    /**
     * Méthode qui importe toutes les classes d'un package
     * @param className Nom de la classe à importer
     * @param packagePath Chemin du package
     */
    private static void addClassToTreeView(String className, String packagePath) {

        if (treeView == null) {
            System.err.println("Erreur : TreeView n'est pas initialisé !");
            return;
        }

        // Initialiser le noeud racine si nécessaire
        if (treeView.getRoot() == null) {
            treeView.setRoot(new TreeItem<>(new PackageNode("Elements importés")));
            System.out.println("Création de la racine du TreeView...");
        }

        // Nom simple de la classe
        String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
        TreeItem<PackageNode> classNode = new TreeItem<>(new PackageNode(simpleClassName));

        // Nom du package
        String directoryName = new File(packagePath).getName();
        TreeItem<PackageNode> packageNode = null;

        // Rechercher ou ajouter le package
        for (TreeItem<PackageNode> node : treeView.getRoot().getChildren()) {
            if (node.getValue().getName().equals(directoryName)) {
                packageNode = node;
                break;
            }
        }

        if (packageNode == null) {
            packageNode = new TreeItem<>(new PackageNode(directoryName));
            treeView.getRoot().getChildren().add(packageNode);
            System.out.println("Ajout du nouveau package : " + directoryName);
        }

        // Ajouter la classe au package
        packageNode.getChildren().add(classNode);
        System.out.println("Ajout de la classe " + simpleClassName + " au package " + directoryName);
    }



    /*
     * ### SETTERS ###
     */
    public static void setTreeView(TreeView<PackageNode> treeView) {
        Import.treeView = treeView;
    }



}
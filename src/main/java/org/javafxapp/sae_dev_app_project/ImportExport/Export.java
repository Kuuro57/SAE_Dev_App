package org.javafxapp.sae_dev_app_project.ImportExport;

import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.imageio.ImageIO;

import static org.javafxapp.sae_dev_app_project.ImportExport.FileManipulator.hasBeenLoaded;

public class Export {


    /**
     * Méthode qui récupère et met en forme toutes les informations d'une classe
     * @param nomClasse Nom de la classe dont on veut les informations
     * @return Les informations de la classe
     */
    public static String getClassInfo(String nomClasse) {

        try {

            // Initialisation de l'affichage final
            StringBuffer affichage = new StringBuffer("Nom de la classe : ").append(nomClasse);
            // On récupère la classe sous forme d'un objet Class depuis son nom


            Class<?> classe = null;
            if (hasBeenLoaded(nomClasse) != null){
                classe = hasBeenLoaded(nomClasse);
            }
            else{
                classe = Class.forName(nomClasse);
            }


            // On récupère les classes que cette classe implémente
            affichage.append("\n\nInterface(s) :");
            for (Class<?> c : classe.getInterfaces()) {
                affichage.append("\n - ").append(c.getName());
            }

            // Si la classe a une classe mère
            if (!(classe.getSuperclass() == null)){
                // On récupère la classe mère de cette classe
                affichage.append("\nClasse mère : ").append(classe.getSuperclass().getName());
            }

            // On récupère les attributs de cette classe
            affichage.append("\n\nAttribut(s) :");
            for (Field att : classe.getDeclaredFields()) {
                // On ajoute l'accessibilité de l'attribut (private, protected, public)
                int numModif = att.getModifiers();
                String nomModif = Modifier.toString(numModif);
                affichage.append("\n - ").append(nomModif);

                // On ajoute le type de l'attribut
                affichage.append(" ").append(att.getGenericType().getTypeName());

                // On ajoute son nom
                affichage.append(" ").append(att.getName());
            }

            // On récupère le(s) constructeur(s) de cette classe
            affichage.append("\n\nConstructeur(s) publique(s) :");
            for (Constructor<?> constructeur : classe.getDeclaredConstructors()) {
                // On affiche le nom du constructeur
                affichage.append("\n - ").append(constructeur.getName());

                // On affiche le type de ses paramètres
                affichage.append("(");
                for (Parameter param : constructeur.getParameters()) {
                    affichage.append(param.getType().getTypeName()).append(",");
                }
                affichage.append(")");
            }

            // On récupère les méthodes de cette classe
            affichage.append("\n\nMéthodes :");
            for (Method methode : classe.getDeclaredMethods()) {
                // On affiche l'accessibilité de la méthode
                int numModif = methode.getModifiers();
                String nomModif = Modifier.toString(numModif);
                affichage.append("\n - ").append(nomModif);

                // On affiche le nom de la méthode
                affichage.append(" ").append(methode.getName());

                // On affiche le type de son/ses paramètre(s)
                affichage.append("(");
                for (Parameter param : methode.getParameters()) {
                    affichage.append(param.getType().getTypeName()).append(",");
                }
                affichage.append(")");

                // On récupère le type de renvoie de la méthode
                affichage.append(" : ").append(methode.getReturnType());
            }

            // On retourne l'affichage
            return affichage.toString();

        }
        catch (ClassNotFoundException e) {
            return "La classe n'a pas été trouvée";
        }

    }



    /**
     * Méthode qui renvoie le squelette d'une classe
     * @param nomClasse Nom de la classe dont on veut le squelette
     */
    public static String exportInJava(String nomClasse) {

        try {

            // Initialisation de l'affichage final
            StringBuffer aff = new StringBuffer();
            // On récupère la classe sous forme d'un objet Class depuis son nom
            Class<?> classe = null;
            if (hasBeenLoaded(nomClasse) != null){
                classe = hasBeenLoaded(nomClasse);
            }
            else{
                classe = Class.forName(nomClasse);
            }

            // On récupère et ajoute à l'affichage le type d'accessibilité de la classe
            String nomTypeClasse = Modifier.toString(classe.getModifiers());
            aff.append(nomTypeClasse);

            // On récupère et ajoute à l'affichage le type de fichier (classe (abstraite), interface)
            if (classe.isInterface()) {
                aff.append(" interface");
            }
            else {
                aff.append(" class ");
            }

            // On ajoute à l'affichage le nom de la classe
            aff.append(removePackageName(nomClasse));

            // Si la classe implemente une interface (ou plusieurs)
            if (classe.getInterfaces().length != 0) {

                aff.append(" implements ");
                // On boucle sur les interfaces
                for (Class<?> c : classe.getInterfaces()) {
                    // On ajoute à l'affichage le nom de la classe + ", "
                    aff.append(removePackageName(c.getName())).append(", ");
                }
                // On retire la dernière virgule
                removeLastComa(aff);

            }

            // Si la classe a une classe mère
            if (!(classe.getSuperclass() == null)) {
                // Si la classe n'hérite pas de la classe Object
                if (!(classe.getSuperclass().getName().equals("java.lang.Object"))) {
                    // On ajoute " extends" + le nom de la classe
                    aff.append(" extends ").append(classe.getSuperclass().getSimpleName());
                }
            }

            // On ajoute le "{"
            aff.append(" {\n");

            // On récupère le(s) attribut(s) de la classe (accessibilité, type, nom) + ";"
            for (Field att : classe.getDeclaredFields()) {
                aff.append("\n");
                String nomAccessAttribut = Modifier.toString(att.getModifiers());
                String typeAttribut = removePackageName(att.getGenericType().getTypeName());
                String nomAttribut = att.getName();

                // On ajoute l'attribut à l'affichage
                aff.append("\t").append(nomAccessAttribut).append(" ").append(typeAttribut).append(" ").append(nomAttribut).append(";");
            }

            aff.append("\n");

            // On récupère le(s) constructeur(s) publique de la classe (public, nom, type de paramètre(s)) + "{}"
            for (Constructor<?> constructeur : classe.getDeclaredConstructors()) {
                aff.append("\n\tpublic ");
                String nomConstructeur = removePackageName(constructeur.getName());
                StringBuffer parametres = new StringBuffer("(");

                // On boucle sur les paramètres du constructeur
                for (Parameter param : constructeur.getParameters()) {
                    String nomAtt = param.getName();
                    parametres.append(param.getType().getSimpleName()).append(" ").append(nomAtt).append(", ");
                }

                // On retire la dernière virgule
                removeLastComa(parametres);

                // On ajoute le constructeur à l'affichage
                aff.append(nomConstructeur).append(parametres).append(") {}");
            }

            // On récupères le(s) méthode(s) de la classe (accessibilité, type de retour, nom, paramètre(s)) + "{}"
            for (Method methode : classe.getDeclaredMethods()) {
                String access = Modifier.toString(methode.getModifiers());
                String typeRetour = removePackageName(methode.getReturnType().getTypeName());
                String nom = methode.getName();
                StringBuffer parametres = new StringBuffer("(");

                // On récupère tous les paramètres
                for (Parameter param : methode.getParameters()) {
                    String nomAtt = param.getName();
                    parametres.append(param.getType().getSimpleName()).append(" ").append(nomAtt).append(", ");
                }
                // On retire la dernière virgule
                removeLastComa(parametres);

                // On ajoute la méthode à l'affichage
                aff.append("\n\n\t").append(access).append(" ").append(typeRetour).append(" ").append(nom).append(parametres).append(") {}");
            }


            createJavaFile(aff.toString(), nomClasse);

            // On retourne l'affichage
            return aff.append("\n\n}").toString();

        }
        catch (ClassNotFoundException e) {
            return "Classe non trouvée";
        }

    }



    /**
     * Méthode qui exporte une capture d'écran de l'application au format PNG dans le répertoire path
     * @param view Vue contenant toutes les classes
     */
    public static void exportInPNG(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openRepositoryPathAndFileNameChooser();

        // On prend un screenshot de l'application
        WritableImage image = view.snapshot(null, null);

        // Convertir WritableImage en BufferedImage
        BufferedImage bufferedImage = new BufferedImage(
                (int) image.getWidth(),
                (int) image.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // Conversion du WritableImage en BufferedImage
        for (int y = 0; y < (int) image.getHeight(); y++) {
            for (int x = 0; x < (int) image.getWidth(); x++) {
                int argb = image.getPixelReader().getArgb(x, y);
                bufferedImage.setRGB(x, y, argb);
            }
        }

        try {
            // Enregistrer l'image au format PNG
            ImageIO.write(bufferedImage, "png", file);
        }
        catch (IOException e) {
            System.out.println("erreur");
        }


    }



    /**
     * Méthode qui construit un fichier PlantUml à partir d'une classe Java
     * @param nomClasse Nom de la classe que l'on veut modéliser en PlantUml
     * @param nomFichierPlantUml Nom du fichier qui contiendra le code PlantUml
     * @return String contenant le code PlantUml
     */
    public static String exportInPUml(String nomClasse, String nomFichierPlantUml) {

        try {

            // Initialisation de l'affichage final
            StringBuffer aff = new StringBuffer("@startuml\n");
            // On récupère la classe sous forme d'un objet Class depuis son nom
            Class<?> classe = null;
            if (hasBeenLoaded(nomClasse) != null){
                classe = hasBeenLoaded(nomClasse);
            }
            else{
                classe = Class.forName(nomClasse);
            }

            // Si la classe est une interface
            if (classe.isInterface()) {
                // On déclare l'interface
                aff.append("interface ").append(classe.getSimpleName()).append(" {");
            }
            // Sinon si la classe est abstraite
            else if (Modifier.toString(classe.getModifiers()).contains("abstract")) {
                // On déclare la classe abstraite
                aff.append("abstract class ").append(classe.getSimpleName()).append(" {");
            }
            // Sinon
            else {
                // On déclare la classe
                aff.append("class ").append(classe.getSimpleName()).append(" {");
            }

            // On récupère et affiche dans l'affichage les attributs de cette classe
            for (Field att : classe.getDeclaredFields()) {

                // On retourne à la ligne
                aff.append("\n\t");

                // On récupère les informations utilses
                String access = convertPUmlAccess(att.getModifiers());
                String typeAtt = removePackageName(att.getGenericType().getTypeName());
                String nom = " " + att.getName();

                // On affiche l'attribut
                aff.append(access).append(typeAtt).append(nom);

            }

            // On récupère et affiche le(s) constructeur(s) de cette classe
            for (Constructor<?> constructeur : classe.getDeclaredConstructors()) {

                aff.append("\n\t");

                // On récupère les informations utilses
                String access = convertPUmlAccess(constructeur.getModifiers());
                String nom = removePackageName(constructeur.getName());
                StringBuffer parametres = new StringBuffer("(");

                // On récupère le type du/des paramètre(s) du constructeur
                for (Parameter param : constructeur.getParameters()) {
                    parametres.append(param.getType().getSimpleName()).append(", ");
                }
                // On enlève la dernière virgule
                removeLastComa(parametres);
                // On ajoute ")"
                parametres.append(")");

                // On affiche le constructeur
                aff.append(access).append(nom).append(parametres);

            }

            // On récupère et affiche les méthodes de cette classe
            for (Method methode : classe.getDeclaredMethods()) {

                aff.append("\n\t");

                // On récupère les informations utilses
                String access = convertPUmlAccess(methode.getModifiers());
                String nom = removePackageName(methode.getName());
                StringBuffer parametres = new StringBuffer("(");
                String typeRetour = removePackageName(methode.getReturnType().getTypeName());

                // On récupère le type du/des paramètre(s) de la méthode
                for (Parameter param : methode.getParameters()) {
                    parametres.append(param.getType().getSimpleName()).append(", ");
                }
                // On enlève la dernière virgule
                removeLastComa(parametres);
                // On ajoute ")"
                parametres.append(")");

                // On affiche la méthode
                aff.append(access).append(nom).append(parametres).append(" : ").append(typeRetour);

            }

            // On ajoute le "@enduml"
            aff.append("\n}\n@enduml");

            // On insert le code généré dans un fichier
            createPUmlFile(aff.toString(), nomFichierPlantUml);

            // On retourne l'affichage final
            return aff.toString();

        }
        catch (ClassNotFoundException e) {
            return "La classe n'a pas été trouvée";
        }

    }



    /**
     * Méthode qui retourne le bon caractère pour représenter l'accès en UML (+, -, #, {abstract})
     * @param accessInt Entier qui représente le type d'accès
     * @return Le bon caratère représentant l'accès
     */
    private static String convertPUmlAccess(int accessInt) {

        String access = Modifier.toString(accessInt);
        StringBuffer res = new StringBuffer();

        // Si l'attribut (ou la méthode) est private
        if (access.contains("private")) {
            // On commence par "- "
            res.append("- ");
        }
        // Sinon si l'attribut (ou la méthode) est protected
        else if (access.contains("protected")) {
            // On commence par "# "
            res.append("# ");
        }
        // Sinon
        else {
            // On commence par "+ "
            res.append("+ ");
        }

        // Si l'attribut (ou la méthode) est static
        if (access.contains("static")) {
            // On ajoute "{static} "
            res.append("{static} ");
        }

        // Si la méthode est abstract
        if (access.contains("abstract")) {
            // On ajoute "{abstract} "
            res.append("{abstract} ");
        }

        // On retourne le résultat
        return res.toString();

    }







    /**
     * Méthode qui retire le package du nom
     * @param txt La chaîne à traiter
     * @return Le nom traité
     */
    public static String removePackageName(String txt) {

        // Si le text contient des "<>"
        if (txt.contains("<") && txt.contains(">")) {
            // On récupère la classe principale
            String classePrincipale = txt.substring(0, txt.indexOf("<"));

            // On récupère la classe entre "<>"
            int indexDeb = txt.indexOf("<");
            int indexFin = txt.indexOf(">");
            String classeUtilisee = txt.substring(indexDeb, indexFin);

            // On récupère le nom seul de la classe principale
            String[] res1 = classePrincipale.split("\\.");
            classePrincipale = res1[res1.length - 1];

            // On récupère le nom seul de la classe entre "<>"
            String[] res2 = classeUtilisee.split("\\.");
            classeUtilisee = res2[res2.length - 1];

            // On retourne le nom simple de la classe principale et de la classe utilisée
            return classePrincipale + "<" + classeUtilisee + ">";
        }
        // Sinon
        else {
            // On retourne le nom seul de la classe
            String[] res = txt.split("\\.");
            return res[res.length - 1];
        }

    }



    /**
     * Méthode qui retire la dernière virgule d'une chaîne de caractère (et l'espace qui suit)
     * @param txt La chaîne à traiter
     */
    private static void removeLastComa(StringBuffer txt) {

        // Si le texte contient une virgule
        if (txt.toString().contains(",")) {
            // On récupère l'index là où se trouve la dernière virgule
            int index = txt.lastIndexOf(",");
            // On supprime cette virgule et l'espace qui suit
            txt.deleteCharAt(index);
            txt.deleteCharAt(index);
        }

    }



    /**
     * Méthode qui créer un fichier au format PlantUML dans le dossier plantUmlFiles à la racine du projet
     * @param code Lignes de code au format PlantUML à mettre dans le fichier
     * @param nomFichier Nom du fichier qu'aura le nouveau fichier
     */
    private static void createPUmlFile(String code, String nomFichier) {

        try {

            // On test le nom du fichier pour ne pas avoir de doublons
            nomFichier = testForValidFileName(nomFichier, ".puml", "plantUmlFiles/");

            // On initialise de façon abstraite le fichier dans le répertoire /plantUmlFiles
            // en ajoutant l'extension ".puml"
            File fichier = new File("plantUmlFiles/" + nomFichier + ".puml");

            // On ouvre le fichier et on y insert le code
            FileWriter fichierEcriture = new FileWriter(fichier);
            fichierEcriture.write(code);

            // On ferme le fichier
            fichierEcriture.close();

        }
        // Erreur IOException
        catch (IOException e) {
            System.out.println("Fichier non créé :\n" + e.getMessage());
        }

    }

    // REFACTOR POSSIBLE AVEC LE CODE DE LA METHODE PRECEDENTE

    /**
     * Méthode qui créer un fichier au format Java dans le dossier plantUmlFiles à la racine du projet
     * @param code Lignes de code au format Java à mettre dans le fichier
     * @param nomFichier Nom du fichier qu'aura le nouveau fichier
     */
    private static void createJavaFile(String code, String nomFichier) {

        try {

            // On test le nom du fichier pour ne pas avoir de doublons
            nomFichier = testForValidFileName(nomFichier, ".java", "JavaFrame/");

            // On initialise de façon abstraite le fichier dans le répertoire /plantUmlFiles
            // en ajoutant l'extension ".puml"
            File fichier = new File("JavaFrame/" + nomFichier + ".java");

            // On ouvre le fichier et on y insert le code
            FileWriter fichierEcriture = new FileWriter(fichier);
            fichierEcriture.write(code);

            // On ferme le fichier
            fichierEcriture.close();

        }
        // Erreur IOException
        catch (IOException e) {
            System.out.println("Fichier non créé :\n" + e.getMessage());
        }

    }




    /**
     * Méthode qui remplace le nom du fichier donner pour qu'il n'y est pas de doublon dans le dossier donné
     * @param nomFichier Nom du fichier à tester
     * @param extension Extension du fichier
     * @param cheminDossier Chemin du dossier dans lequel le nom du fichier est tester
     * @return Un String contenant le nom du fichier traité (inchangé si le nom n'est pas présent dans le dossier)
     */
    private static String testForValidFileName(String nomFichier, String extension, String cheminDossier) {

        // On initialise le nouveau nom
        String nouveauNom = nomFichier;

        // On récupère la liste des noms de tous les fichiers dans le dossier donné en paramètre
        File dossier = new File(cheminDossier);
        // vérifie que le dossier existe sinon on le créer
        // cheminDossier
        if (!dossier.exists()) {
            dossier.mkdir();
        }
        ArrayList<String> listeNomFichiers =
                new ArrayList<>(Arrays.asList(Objects.requireNonNull(dossier.list())));

        // On initialise i à 1
        int i = 1;
        // On boucle temps que le nom du fichier (nom + extension) à tester existe déjà dans la liste
        // des noms de fichiers
        while (listeNomFichiers.contains(nouveauNom + extension)) {
            // On change le nom du fichier test -> nomFichier(i)
            nouveauNom = nomFichier + "(" + i + ")";
            // On augmente i de 1
            i++;
        }

        // On retourne le fichier avec le bon nom
        return nouveauNom;

    }

}

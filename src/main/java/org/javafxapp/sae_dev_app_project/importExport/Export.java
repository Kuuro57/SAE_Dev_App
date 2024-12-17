package org.javafxapp.sae_dev_app_project.importExport;

import javafx.scene.image.WritableImage;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.text.View;


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
            if (FileManipulator.hasBeenLoaded(nomClasse) != null){
                classe = FileManipulator.hasBeenLoaded((nomClasse));
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
     * Méthode qui exporte le squelette Java d'une ou plusieurs classes
     * @param view Vue contenant les classes à traiter
     */
    public static void exportInJava(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File directory = fileChooserHandler.openRepositoryPath();

        String codejava;

        try {

            // Boucle pour parcourir toutes les classes du diagramme dans la vue
            for (int i = 0; i < view.getAllClasses().size(); i++) {

                String className = view.getAllClasses().get(i).getName(); // Supposons que `getName()` donne le nom de la classe
                String fileName = className + ".java";

                File file = new File(directory, fileName);

                Writer writer = new FileWriter(file);

                // Ouverture du fichier en mode écriture
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                // On récupère le code puml de la classe actuellement traitée dans la boucle
                codejava = getJavaFrameCode(view.getAllClasses().get(i));

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codejava);

                // Fermeture du fichier
                bufferedWriter.close();

            }

        } catch (IOException e){
            System.out.println(e.getMessage());
        }


    }


    public static String getJavaFrameCode(ModelClass modelClass) {

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        String nomClasse = modelClass.getName();

        // Si la classe traitée est une Classe
        aff.append("class ");

        // On affiche le nom de la classe
        aff.append(nomClasse);

        // Première accolade
        aff.append(" {");

        // Vide pour le pattern
        aff.append("\n");

        // Dernière accolade
        aff.append("}");

        return aff.toString();

    }


    /**
     * Méthode qui exporte une capture d'écran de l'application au format PNG dans le répertoire path
     * @param view Vue contenant toutes les classes
     */
    public static void exportInPNG(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openRepositoryPathAndFileNameChooser("png");

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
        catch (IllegalArgumentException ignored) {}


    }



    /**
     * Méthode qui construit un fichier PlantUml à partir d'une liste de classes Java récupérée de la vue
     */
    public static void exportInPUml(ViewAllClasses view) {

        // Affichage d'un FileChooser pour que l'utilisateur choisisse le chemin et le nom du fichier
        FileChooserHandler fileChooserHandler = new FileChooserHandler();
        File file = fileChooserHandler.openRepositoryPathAndFileNameChooser("codepuml");

        String codepuml;

        try {

            Writer writer = new FileWriter(file);
            // Ouverture du fichier en mode écriture
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // On ajoute le "@startuml"
            bufferedWriter.write("@startuml");

            // Retour à la ligne
            bufferedWriter.newLine();

            // Boucle pour parcourir toutes les classes du diagramme dans la vue
            for (int i = 0; i < view.getAllClasses().size(); i++) {

                // On récupère le code puml de la classe actuellement traitée dans la boucle
                codepuml = getPUmlCode(view.getAllClasses().get(i));

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codepuml);

                // Retour à la ligne
                bufferedWriter.newLine();

            }

            // On ajoute le "@enduml"
            bufferedWriter.write("@enduml");

            // Fermeture du fichier
            bufferedWriter.close();

        } catch (IOException e){
            System.out.println(e.getMessage());
        }

    }


    /**
     * Méthode qui récupère le code plantuml balisé d'une classe à examiner
     * @param modelClass La classe de type ModelClass que l'on veut traiter
     * @return le code plantuml en String
     */
    private static String getPUmlCode(ModelClass modelClass){


        String className = modelClass.getName();

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        // A MODIFIER APRES ITERATION //

        // On affiche le type de classe
        aff.append("class ");

        // On affiche le nom de la classe
        aff.append(className);

        // Accolades
        aff.append("{\n");
        aff.append("}\n");

        //----------------------------//

        return aff.toString();


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
    private static String removePackageName(String txt) {

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

package org.javafxapp.sae_dev_app_project.importExport;

import javafx.scene.image.WritableImage;
import javafx.scene.text.Text;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.*;

import javax.imageio.ImageIO;


/**
 * Classe qui contient toutes les méthodes pour exporter le diagramme sous différents formats
 */
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

                // On récupère le type de renvoi de la méthode
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

        String codeJava;

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
                codeJava = getJavaFrameCode(view.getAllClasses().get(i));

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codeJava);

                // Fermeture du fichier
                bufferedWriter.close();

            }

        } catch (IOException e){
            System.out.println(e.getMessage());
        }


    }



    /**
     * Méthode qui créé le squelette de la classe Java à partir d'un modèle
     * @param modelClass Modèle d'une classe
     * @return Un String contenant le squelette Java
     */
    private static String getJavaFrameCode(ModelClass modelClass) {

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        // Intitué de la classe
        aff.append("class " + modelClass.getName() + " {\n");

        // Parcours de toutes les méthodes de la classe
        for (org.javafxapp.sae_dev_app_project.classComponent.Method m : modelClass.getMethods()){

            aff.append("\n");
            // Ajout de l'en-tête de la méthode
            aff.append(m.getModifier() + " " + FileManipulator.removePackageName(m.getReturnType()) + " " + m.getName() + "(");

            // Parcours des paramètres des méthodes
            for (Parameter param : m.getParameters()) {

                // Ajout des paramètres
                aff.append(FileManipulator.removePackageName(param.getType().getTypeName()) + " " + param.getName());
                aff.append(", ");

            }

            FileManipulator.removeLastComa(aff);

            aff.append("){ ");
            aff.append("}\n");

        }

        // On ferme la classe
        aff.append("\n}");

        return aff.toString();

    }



    /**
     * Méthode qui exporte une capture d'écran de l'application au format PNG
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
     * @param view Vue qui contient la liste de toutes les classes
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
     * Méthode qui récupère le code plantuml balisé d'une classe
     * @param modelClass La classe de type ModelClass que l'on veut traiter
     * @return Le code plantuml en String
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

        String parametres = "";

        for (org.javafxapp.sae_dev_app_project.classComponent.Method m : modelClass.getMethods()) {

            // Si la méthode requiert des paramètres
            if (!m.getParameters().isEmpty()) {

                parametres = ModelClass.displayParams(m.getParameters());

            }

            // On affiche la méthode sur le diagramme de classe
            String nomMethod = new String(FileManipulator.convertModifier(m.getModifier()) + m.getName() + "(" + parametres + ") : " + FileManipulator.removePackageName(m.getReturnType()));

            aff.append(nomMethod + "\n");

        }

        aff.append("}\n");

        //----------------------------//

        return aff.toString();


    }

}

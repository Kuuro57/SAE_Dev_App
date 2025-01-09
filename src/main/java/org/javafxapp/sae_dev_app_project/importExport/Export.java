package org.javafxapp.sae_dev_app_project.importExport;

import javafx.scene.image.WritableImage;
import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.javafxapp.sae_dev_app_project.views.ViewAllClasses;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;


/**
 * Classe qui contient toutes les méthodes pour exporter le diagramme sous différents formats
 */
public class Export {


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

        String extend = "";
        if (modelClass.getExtendedClass() != null) {
            extend = " extends " + removePackageName(modelClass.getExtendedClass().getName());
        }

        String implement = "";
        if (!modelClass.getInheritedClasses().isEmpty()) {
            implement = "implements ";
            for (ModelClass mc : modelClass.getInheritedClasses()) {
                implement = implement + mc.getName() + " ";
            }
        }

        // Intitué de la classe
        aff.append(modelClass.getType() + " " + modelClass.getName() + extend + " " + implement);
        aff.append("{\n");

        // --------------------------- ATTRIBUTS -------------------------------- //
        for (Attribute a : modelClass.getAttributes()){
            String modifiers = "";
            if(!a.getModifier().isEmpty()) {
                modifiers = a.getModifier() + " ";
            }
            aff.append("\n");
            aff.append("    " + modifiers + removePackageName(a.getType()) + " " + a.getName() + ";\n");
        }

        aff.append("\n");

        // --------------------------- CONSTRUCTEUR -------------------------------- //

        for (Constructor c : modelClass.getConstructors()){

            aff.append("\n");
            aff.append("    " + c.getModifier() + " " + c.getName() + "(");

            // Parcours des paramètres des méthodes
            for (Parameter param : c.getParameters()) {
                // Ajout des paramètres
                aff.append(removePackageName(param.getType()) + " " + param.getName());
                aff.append(", ");
            }

            removeLastComa(aff);
            aff.append("){ ");
            aff.append("}\n");

        }


        // --------------------------- METHODES -------------------------------- //

        // Parcours de toutes les méthodes de la classe
        for (Method m : modelClass.getMethods()){

            // Ajout de l'en-tête de la méthode
            aff.append("\n");
            aff.append("    " + m.getModifier() + " " + removePackageName(m.getReturnType()) + " " + m.getName() + "(");

            // Parcours des paramètres des méthodes
            for (Parameter param : m.getParameters()) {
                // Ajout des paramètres
                aff.append(removePackageName(param.getType()) + " " + param.getName());
                aff.append(", ");
            }

            removeLastComa(aff);
            aff.append("){ ");
            aff.append("}\n");

        }
        // On ferme la classe et on retourne l'affichage
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
            // Enregistrement l'image au format PNG
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

                // Retour à la ligne
                bufferedWriter.newLine();

                // Ecriture du code puml récupéré de la classe
                bufferedWriter.write(codepuml);

                // Retour à la ligne
                bufferedWriter.newLine();

                // On ajoute les dépendances entre les classes
                bufferedWriter.write(createDependanciesCardinalities(view, view.getAllClasses().get(i)));
            }

            // Ajouter le "@enduml"
            bufferedWriter.write("@enduml");

            // Fermeture du fichier
            bufferedWriter.close();

            // Supprimer les attributs des classes qui apparaissent dans les dépendances
            removeAttributesInDependencies(file);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static void removeAttributesInDependencies(File file) throws IOException {
        // Lire le contenu du fichier existant
        List<String> lines = Files.readAllLines(file.toPath());
        List<String> modifiedLines = new ArrayList<>();

        // Liste pour stocker les dépendances (nom des attributs utilisés dans les dépendances)
        Set<String> dependencies = new HashSet<>();

        // Extraire les noms des attributs dans les dépendances
        for (String line : lines) {
            if (line.contains("--") || line.contains("->") || line.contains("<-")) {
                String[] parts = line.split("[:|\s]"); // Découper la ligne
                for (String part : parts) {
                    if (!part.isBlank() && !part.equals("--") && !part.equals("->") && !part.equals("<-")) {
                        dependencies.add(part.trim());
                    }
                }
            }
        }

        // Parcourir les lignes pour modifier les classes
        boolean insideClass = false;
        String currentClass = "";
        for (String line : lines) {
            if (line.startsWith("class ")) {
                insideClass = true;
                currentClass = line.split(" ")[1].trim();
                modifiedLines.add(line);
            } else if (insideClass && line.startsWith("}")) {
                insideClass = false;
                modifiedLines.add(line);
            } else if (insideClass) {
                // Vérifier si l'attribut est dans les dépendances
                String attributeName = extractAttributeName(line);
                if (attributeName == null || !dependencies.contains(attributeName)) {
                    modifiedLines.add(line);
                }
            } else {
                modifiedLines.add(line);
            }
        }

        // Réécrire le fichier avec les modifications
        Files.write(file.toPath(), modifiedLines, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private static String extractAttributeName(String line) {
        // Exemple: "String name" => retourne "name"
        String[] parts = line.trim().split(" ");
        if (parts.length >= 2) {
            return parts[1].replace(";", "").trim();
        }
        return null;
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

        ArrayList<String> modifierClass = modelClass.hashType();

        if (modifierClass.get(1) != null){
            aff.append(modifierClass.get(1) + " ");
        }

        // On affiche le type de classe
        aff.append(modifierClass.get(2) + " ");

        // On affiche le nom de la classe
        aff.append(className);

        // Accolades
        aff.append("{\n");

        // ----------------------------- ATTRIBUTS ------------------------------- //

        String modifiers = "";

        // Parcours de toutes les méthodes
        for (Attribute a : modelClass.getAttributes()) {

            // Si l'attribut a des modifiers
            if (!a.getModifier().isEmpty()) {
                modifiers = convertModifier(a.getModifier());
            // S'il n'en a pas
            } else {
                modifiers = "# "; // Protected
            }

            // On affiche la méthode sur le diagramme de classe
            String nomMethod = new String(" " + modifiers + a.getName() + " : " + removePackageName(a.getType()));

            aff.append(nomMethod + "\n");

        }

        // ----------------------------- CONSTRUCTEUR ------------------------------- //

        for (org.javafxapp.sae_dev_app_project.classComponent.Constructor c : modelClass.getConstructors()) {

            String parametres = "";
            if (!c.getParameters().isEmpty()) {
                parametres = ModelClass.displayParams(c.getParameters());
            }

            String nomConstruct = new String(" " + convertModifier(c.getModifier()) + c.getName() + "(" + parametres + ")");
            aff.append(nomConstruct + "\n");

        }

        // ----------------------------- METHODES ------------------------------- //

        // Parcours de toutes les méthodes
        for (Method m : modelClass.getMethods()) {

            String parametres = "";

            // Si la méthode requiert des paramètres
            if (!m.getParameters().isEmpty()) {
                parametres = ModelClass.displayParams(m.getParameters());
            }

            // On affiche la méthode sur le diagramme de classe
            String nomMethod = new String(" " + convertModifier(m.getModifier()) + m.getName() + "(" + parametres + ") : " + removePackageName(m.getReturnType()));

            aff.append(nomMethod + "\n");
        }

        aff.append("}\n");


        // ----------------------------- DEPENDANCES ------------------------------- //

        // Classes implémentées
        for (ModelClass m : modelClass.getInheritedClasses()) {
            aff.append(modelClass.getName() + " ..|> " + m.getName() + "\n");
        }

        // Classe héritée
        ModelClass m_extended = modelClass.getExtendedClass();
        if (m_extended != null) {
            aff.append(modelClass.getName() + " --> " + m_extended.getName() + "\n");
        }
        // On retourne l'affichage final
        return aff.append("\n").toString();
    }

    /**
     * Méthode qui retourne le bon caractère pour représenter l'accès en UML (+, -, #, {abstract})
     * @param access Il represente le type d'accès
     * @return Le bon caractère représentant l'accès
     */
    public static String convertModifier(String access) {

        StringBuffer res = new StringBuffer();

        // Utilisé pour l'affichage en cas de -
        if (access.contains("-")){
            access = access.replace("-", "");
            convertModifier(access);
        }

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
        // Sinon si l'attribut est public
        else if (access.contains("public")) {
            res.append("+ ");
        }
        // Sinon
        else {
            // On commence par "+ "
            res.append("# ");
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

        //Si un attribut est final
        if (access.contains("final")) {
            // On ajoute "{final}"
            res.append("{final} ");
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
            String classeUtilisee = txt.substring(indexDeb + 1, indexFin);

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
    public static void removeLastComa(StringBuffer txt) {
        // Si le texte contient une virgule
        if (txt.toString().contains(",")) {
            // On récupère l'index là où se trouve la dernière virgule
            int index = txt.lastIndexOf(",");
            // On supprime cette virgule et l'espace qui suit
            txt.deleteCharAt(index);
        }

    }

    /*
    * méthode createDependanciesCardinalities méthodes qui permet de créer les dépendances entre les classes
    * avec les cardinalités entre les classes, enlève également l'attribut du corps de la classe pour le mettre
    * dans la dépendance
    * @param view Vue contenant toutes les classes
    * @param modelClass Modèle d'une classe
    *
    * @return String contenant les dépendances entre les classes
    */

    public static String createDependanciesCardinalities(ViewAllClasses view, ModelClass modelClass) {

        // Initialisation de l'affichage final
        StringBuffer aff = new StringBuffer();

        // Parcours de toutes les classes
        for (ModelClass m : view.getAllClasses()) {

            // Si la classe actuelle est différente de la classe en cours
            if (!m.equals(modelClass)) {

                // On récupère les dépendances entre les classes
                // càd si l'attribut de la classe actuelle est de type d'une classe de la liste
                // boucle sur les attributs de la classe actuelle pour voir si le type de l'attribut est égal à la classe en cours
                for (Attribute a : modelClass.getAttributes()) {

                    // Si le type de l'attribut est égal à la classe en cours
                    if (a.getType().equals(m.getName())) {

                        // On ajoute la dépendance à l'affichage final
                        aff.append(modelClass.getName() + " --> \"1\" " + m.getName() + " : " + Export.convertModifier(a.getModifier()) + a.getName() + "\n");

                    }

                    // cas des attribut de type collection qui prennent en paramètre une classe de la liste (regex)
                    // Si le type de l'attribut est de type collection
                    if (Pattern.matches(".*<.*>", a.getType())) {

                        // On récupère le type de la collection
                        String type = a.getType().substring(a.getType().indexOf("<") + 1, a.getType().indexOf(">"));

                        // Si le type de la collection est égal à la classe en cours
                        if (type.equals(m.getName())) {

                            // On ajoute la dépendance à l'affichage final
                            aff.append(modelClass.getName() + " \"1\" -> \"*\" " + m.getName() + " : " + Export.convertModifier(a.getModifier()) + a.getName() + "\n");

                        }

                    }

                }

            }

        }
        // On retourne l'affichage final
        return aff.toString();
    }


}

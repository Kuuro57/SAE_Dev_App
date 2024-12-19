package org.javafxapp.sae_dev_app_project.importExport;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FileManipulator {

    /**
     * Méthode qui vérifie si une classe a déjà été chargée
     *
     * @param nomClasse Nom de la classe à vérifier
     * @return La classe si elle a déjà été chargée, null sinon
     */

    public static Class<?> hasBeenLoaded(String nomClasse) {
        Class<?> loadedClass = null;
        Set<Class<?>> loadedClasses = CustomClassLoader.getLoadedClasses();
        // conversion du set en liste
        List<Class<?>> list = new ArrayList<>(loadedClasses);
        // parcours de la liste
        for (Class<?> cl : list) {
            // si la classe est déjà chargée
            if (cl.getName().equals(nomClasse)) {
                // on retourne la classe
                loadedClass = cl;
            }

        }

        return loadedClass;

    }


    /**
     * Méthode qui retourne le bon caractère pour représenter l'accès en UML (+, -, #, {abstract})
     *
     * @param access Il represente le type d'accès
     * @return Le bon caractère représentant l'accès
     */
    public static String convertModifier(String access) {

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

        // On retourne le résultat
        return res.toString();
    }


    /**
     * Méthode qui retire le package du nom
     *
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
     *
     * @param txt La chaîne à traiter
     */
    public static void removeLastComa(StringBuffer txt) {

        // Si le texte contient une virgule
        if (txt.toString().contains(",")) {
            // On récupère l'index là où se trouve la dernière virgule
            int index = txt.lastIndexOf(",");
            // On supprime cette virgule et l'espace qui suit
            txt.deleteCharAt(index);
            txt.deleteCharAt(index);
        }

    }
}
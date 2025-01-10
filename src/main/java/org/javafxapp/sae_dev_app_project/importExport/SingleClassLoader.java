package org.javafxapp.sae_dev_app_project.importExport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SingleClassLoader {

    // Attributs
    public static final Set<Class<?>> LOADED_CLASSES = new HashSet<>(); // Set qui contient les classes chargées



    /**
     * Méthode qui charge une classe depuis un fichier .class
     * @param file Fichier .class à charger
     * @param rootDirectory Répertoire racine des packages (dossier de base pour déterminer le package)
     * @return L'objet Class correspondant
     */
    public Class<?> loadClassFromFile(File file, File rootDirectory) throws FileNotFoundException {

        // Initialisation des variables
        boolean isCharged = false;
        Class<?> clas = null;
        String[] tmp = rootDirectory.getAbsolutePath().split(Pattern.quote(File.separator)); // utilisation de quote pour éviter les erreurs de regex et support de windows et linux
        String[] tmp2 = file.getAbsolutePath().split(Pattern.quote(File.separator));
        String nameClass = tmp2[tmp2.length - 1];


        // On boucle sur tout les noms de fichiers possibles
        for (int i = 0; i < tmp.length; i++) {

            // On récupère le path à tester pour l'import
            StringBuilder newRootDirBuilder = new StringBuilder();
            for (int j = 0; j < tmp.length - i; j++) {
                newRootDirBuilder.append(tmp[j]).append(File.separator);
            }
            String newRootDir = newRootDirBuilder.toString();


            // On récupère le nom du fichier à tester pour l'import
            StringBuilder nameWithPackageBuilder = new StringBuilder();
            for (int j = tmp.length - i; j < tmp2.length - 1; j++) {
                nameWithPackageBuilder.append(tmp[j]).append(".");
            }
            nameWithPackageBuilder.append(nameClass.replace(".class", ""));
            String nameWithPackage = nameWithPackageBuilder.toString();


            // On test le chargement de la classe
            try {

                rootDirectory = new File(newRootDir);
                URL[] urls = {rootDirectory.toURI().toURL()};
                URLClassLoader urlClassLoader = new URLClassLoader(urls);

                clas = urlClassLoader.loadClass(nameWithPackage);

                // Booléen à true (tout s'est bien passé)
                isCharged = true;

            }
            catch (ClassNotFoundException | NoClassDefFoundError | MalformedURLException ignored) {}


            // Si la classe est chargée, on renvoie l'objet Class
            if (isCharged) {
                LOADED_CLASSES.add(clas);
                return clas;
            }


        }

        throw new FileNotFoundException("Impossible de charger la classe");


    }



    /**
     * Méthode utilitaire pour vérifier si une classe est déjà chargée
     * @param name Nom complet de la classe (fully qualified name)
     * @return True si la classe est déjà chargée, false sinon
     */
    public static boolean hasBeenLoaded(String name) {
        for (Class<?> c : LOADED_CLASSES) {
            if (c.getSimpleName().equals(name)) {
                return true;
            }
        }
        return false;
    }



    /**
     * Méthode utilitaire pour récupérer une classe chargée
     * @param name Nom complet de la classe (fully qualified name)
     * @return L'objet Class correspondant, ou null si la classe n'est pas chargée
     */
    public static Class<?> getClassForName(String name) {
        for (Class<?> c : LOADED_CLASSES) {
            if (c.getSimpleName().equals(name)) {
                return c;
            }
        }
        return null;
    }

}

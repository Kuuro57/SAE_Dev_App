package org.javafxapp.sae_dev_app_project.importExport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import java.util.HashSet;
import java.util.Set;

public class SingleClassLoader {

    // Attributs
    public static final Set<Class<?>> LOADED_CLASSES = new HashSet<>(); // Set qui contient les classes chargées



    /**
     * Charge une classe depuis un fichier .class
     *
     * @param file Fichier .class à charger
     * @param rootDirectory Répertoire racine des packages (dossier de base pour déterminer le package)
     * @return L'objet Class correspondant
     * @throws IOException Si une erreur de lecture survient
     */
    public Class<?> loadClassFromFile(File file, File rootDirectory) throws Exception {

        // Initialisation des variables
        boolean isCharged = false;
        Class<?> clas = null;
        String[] tmp = rootDirectory.getAbsolutePath().split("\\\\");
        String[] tmp2 = file.getAbsolutePath().split("\\\\");
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
            catch (ClassNotFoundException | NoClassDefFoundError ignored) {}


            // Si la classe est chargée, on renvoie l'objet Class
            if (isCharged) {
                LOADED_CLASSES.add(clas);
                return clas;
            }


        }

        throw new ClassNotFoundException("Le fichier est invalide");


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

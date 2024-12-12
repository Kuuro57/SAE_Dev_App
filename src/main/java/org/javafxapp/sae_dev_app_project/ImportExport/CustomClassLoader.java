package org.javafxapp.sae_dev_app_project.ImportExport;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class CustomClassLoader extends ClassLoader {

    private String classPath;
    private static final Set<Class<?>> loadedClasses = new HashSet<>(); // Pour suivre les classes chargées

    public CustomClassLoader(String classPath) {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // Convertir le nom de la classe en chemin de fichier
            String classFile = classPath + File.separator + name.replace('.', File.separatorChar) + ".class";

            // Lire les octets du fichier .class
            byte[] classBytes = Files.readAllBytes(Paths.get(classFile));

            // Définir la classe dans la JVM
            Class<?> clazz = defineClass(name, classBytes, 0, classBytes.length);
            loadedClasses.add(clazz); // Ajouter la classe à la liste des classes chargées

            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }

    // Méthode pour récupérer les classes chargées
    public static Set<Class<?>> getLoadedClasses() {
        return loadedClasses;
    }


    }


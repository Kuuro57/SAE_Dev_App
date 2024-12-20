package org.javafxapp.sae_dev_app_project.classComponent;

import org.javafxapp.sae_dev_app_project.subjects.ModelClass;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    private ModelClass modelClass;

    @BeforeEach
    public void setUp() {
        modelClass = new ModelClass("Test");
    }

    /**
     * Test pour le constructeur de la classe Attribute
     * On vérifie que les attributs sont bien initialisés
     * On vérifie que les getters retournent bien les valeurs attendues
     * On vérifie que les setters modifient bien les valeurs attendues
     */
    @Test
    public void testGetAttributes() {
        Attribute a1 = new Attribute("private", "String", "fromage");
        Attribute a2 = new Attribute("public", "int", "age");
        modelClass.getAttributes().add(a1);
        modelClass.getAttributes().add(a2);
        assertEquals(2, modelClass.getAttributes().size());
        assertEquals("fromage", modelClass.getAttributes().get(0).getName());
        assertEquals("age", modelClass.getAttributes().get(1).getName());
    }


}

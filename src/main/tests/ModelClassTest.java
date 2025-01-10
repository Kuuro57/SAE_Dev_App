import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.javafxapp.sae_dev_app_project.subjects.ModelClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class ModelClassTest {

    private org.javafxapp.sae_dev_app_project.subjects.ModelClass modelClass;



    @Test
    public void testSetHidden() {
        modelClass.setVisibility(false);
        assertFalse(modelClass.isVisible(), "The model class should be hidden");

        modelClass.setVisibility(false);
        assertFalse(modelClass.isVisible(), "The model class should not be hidden");
    }


        @BeforeEach
        public void setUp() {
            modelClass = new ModelClass(
                    "TestModelClass",
                    new ArrayList<>(),
                    new ArrayList<>(),
                    new ArrayList<>(),
                    "class"
            );
        }

        @Test
        public void testSetVisibility() {
            modelClass.setVisibility(false);
            assertFalse(modelClass.isVisible(), "The model class should be hidden");

            modelClass.setVisibility(true);
            assertTrue(modelClass.isVisible(), "The model class should be visible");
        }

        @Test
        public void testAddMethod() {
            Method method = new Method("testMethod", "public", new ArrayList<>(), "void");
            modelClass.getMethods().add(method);
            assertTrue(modelClass.getMethods().contains(method), "The method should be added to the model class");
        }

        @Test
        public void testRemoveMethod() {
            Method method = new Method("testMethod", "public", new ArrayList<>(), "void");
            modelClass.getMethods().add(method);
            modelClass.getMethods().remove(method);
            assertFalse(modelClass.getMethods().contains(method), "The method should be removed from the model class");
        }

        @Test
        public void testGetInheritedClasses() {
            ModelClass inheritedClass = new ModelClass("InheritedClass");
            modelClass.getInheritedClasses().add(inheritedClass);
            assertTrue(modelClass.getInheritedClasses().contains(inheritedClass), "The inherited class should be in the list");
        }

        @Test
        public void testGetExtendedClass() {
            ModelClass extendedClass = new ModelClass("ExtendedClass");
            modelClass.setExtendedClass(extendedClass);
            assertEquals(extendedClass, modelClass.getExtendedClass(), "The extended class should be set correctly");
        }

        @Test
        public void testHideAllAttributes() {
            Attribute attribute = new Attribute("testAttribute", "String", "private");
            modelClass.getAttributes().add(attribute);
            modelClass.hideAllAttributes();
            assertTrue(attribute.isHidden(), "All attributes should be hidden");
        }

        @Test
        public void testShowAllAttributes() {
            Attribute attribute = new Attribute("testAttribute", "String", "private");
            attribute.setHidden(true);
            modelClass.getAttributes().add(attribute);
            modelClass.showAllAttributes();
            assertFalse(attribute.isHidden(), "All attributes should be visible");
        }

        @Test
        public void testHideAllMethods() {
            Method method = new Method("testMethod", "public", new ArrayList<>(), "void");
            modelClass.getMethods().add(method);
            modelClass.hideAllMethods();
            assertTrue(method.isHidden(), "All methods should be hidden");
        }

        @Test
        public void testShowAllMethods() {
            Method method = new Method("testMethod", "public", new ArrayList<Parameter>(), "void");
            method.setHidden(true);
            modelClass.getMethods().add(method);
            modelClass.showAllMethods();
            assertFalse(method.isHidden(), "All methods should be visible");
        }
    }




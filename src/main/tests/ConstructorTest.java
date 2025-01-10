import org.javafxapp.sae_dev_app_project.classComponent.Constructor;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ConstructorTest {

    private Constructor constructor;

    @BeforeEach
    public void setUp() {
        constructor = new Constructor("public", "TestConstructor", new ArrayList<>());
    }

    @Test
    public void testSetHidden() {
        constructor.setHidden(true);
        assertTrue(constructor.isHidden(), "The constructor should be hidden");

        constructor.setHidden(false);
        assertFalse(constructor.isHidden(), "The constructor should not be hidden");
    }

    @Test
    public void testGetName() {
        assertEquals("TestConstructor", constructor.getName(), "The constructor name should be 'TestConstructor' but was " + constructor.getName());
    }

    @Test
    public void testGetModifier() {
        assertEquals("public", constructor.getModifier(), "The constructor modifier should be 'public'");
    }

    @Test
    public void testGetParameters() {
        assertNotNull(constructor.getParameters(), "The constructor parameters should not be null");
    }
}
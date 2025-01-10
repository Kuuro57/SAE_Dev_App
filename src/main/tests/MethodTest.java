import org.javafxapp.sae_dev_app_project.classComponent.Method;
import org.javafxapp.sae_dev_app_project.classComponent.Parameter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MethodTest {

    private Method method;

    @BeforeEach
    public void setUp() {
        method = new Method("public","testMethod" , new ArrayList<>(), "void");
    }

    @Test
    public void testSetHidden() {
        method.setHidden(true);
        assertTrue(method.isHidden(), "The method should be hidden");

        method.setHidden(false);
        assertFalse(method.isHidden(), "The method should not be hidden");
    }

    @Test
    public void testGetName() {
        assertEquals("testMethod", method.getName(), "The method name should be 'testMethod'");
    }

    @Test
    public void testGetModifier() {
        assertEquals("public", method.getModifier(), "The method modifier should be 'public'");
    }

    @Test
    public void testGetReturnType() {
        assertEquals("void", method.getReturnType(), "The method return type should be 'void'");
    }

    @Test
    public void testGetParameters() {
        assertNotNull(method.getParameters(), "The method parameters should not be null");
    }
}
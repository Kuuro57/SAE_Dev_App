import org.javafxapp.sae_dev_app_project.classComponent.Attribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AttributeTest {

    private Attribute attribute;

    @BeforeEach
    public void setUp() {
        attribute = new Attribute("private", "String", "testAttribute");
    }

    @Test
    public void testSetHidden() {
        attribute.setHidden(true);
        assertTrue(attribute.isHidden(), "The attribute should be hidden");

        attribute.setHidden(false);
        assertFalse(attribute.isHidden(), "The attribute should not be hidden");
    }

    @Test
    public void testGetName() {
        assertEquals("testAttribute", attribute.getName(), "The attribute name should be 'testAttribute'");
    }

    @Test
    public void testGetType() {
        assertEquals("String", attribute.getType(), "The attribute type should be 'String'");
    }

    @Test
    public void testGetModifier() {
        assertEquals("private", attribute.getModifier(), "The attribute modifier should be 'private'");
    }


    @Test
    public void testGetDisplay() {
        assertNotNull(attribute.getDisplay(), "The getDisplay method should return a non-null HBox");
    }

}
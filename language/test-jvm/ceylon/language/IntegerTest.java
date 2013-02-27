package ceylon.language;

import static ceylon.language.parseInteger_.parseInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class IntegerTest {

    @Test
    public void testConversionToInteger() {
        assertEquals(0.0, Integer.instance(0).getFloat(), 0.0);
        assertEquals(1.0, Integer.instance(1).getFloat(), 1.0);
        assertEquals(9007199254740991.0, Integer.instance(9007199254740991L).getFloat(), 0.0);
        assertEquals(-9007199254740991.0, Integer.instance(-9007199254740991L).getFloat(), 0.0);
        
        try {
            Integer.instance(9007199254740992L).getFloat();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Integer.instance(-9007199254740992L).getFloat();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
    }
}

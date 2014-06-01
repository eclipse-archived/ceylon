package ceylon.language;

import static ceylon.language.parseFloat_.parseFloat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class FloatTest {

    private void assertParseFloat(Double expect, java.lang.String str) {
        Float parsed = parseFloat(str);
        if (expect != null) {
            if (parsed == null) {
                fail(str + " didn't parse as a Float");
            }
            assertTrue("parseFloat(" + str + ") expected " + expect + " but got " + parsed.doubleValue(), 
                    expect.doubleValue() == parsed.doubleValue());
        } else {
            assertNull("parseFloat(" + str + ") returned a result " + parsed, parsed);
        }
    }
    
    @Test
    public void testParseFloat() {
        assertParseFloat(12.34e3, "12.34e3");
        assertParseFloat(12.34e3, "12.340e3");
        assertParseFloat(12.34e3, "123.4e2");
        assertParseFloat(12.34e3, "1234.0e1");
        assertParseFloat(12.34e3, "1234.0e+1");
        assertParseFloat(12.34e3, "12340.0e0");
        assertParseFloat(12.34e3, "12340.0");
        assertParseFloat(12.34e3, "12340.0");
        assertParseFloat(12.34e3, "123400.0e-1");
        
        assertParseFloat(12.34e3, "012340");
        assertParseFloat(12.34e3, "+12340");
        
        assertParseFloat(-12.34e3, "-12340");
        
        assertParseFloat(1e3, "1.0e3");
        assertParseFloat(1e3, "1.0E3");
        assertParseFloat(1e3, "+1.0e+3");
        assertParseFloat(1e3, "+1.0E+3");
        assertParseFloat(1e3, "1.0k");
        
        assertParseFloat(1e-3, "1.0m");
        assertParseFloat(1e-3, "1m");
        assertParseFloat(1e-3, "1.0e-3");
        assertParseFloat(1e-3, "1.0E-3");
        assertParseFloat(1e-6, "0.000_001");
        assertParseFloat(1e-9, "0.000_000_001");
        
        assertParseFloat(1e3, "+1.0E+3");
        
        assertParseFloat(1e3, "+1.000_000E+3");
        assertParseFloat(1e3, "+1_000.0");
        assertParseFloat(1e6, "+1_000_000.0");
        assertParseFloat(1e6, "+1000000.000_000_000");
        assertParseFloat(1e6, "+1000000.000_000_000e000_000");
        
        assertParseFloat(Double.POSITIVE_INFINITY, "+1.0E+1_000");
        
        assertParseFloat(null, "1E+3");
        assertParseFloat(null, "1e+3");
        assertParseFloat(null, "1e+1_00");
        assertParseFloat(null, "1e+1000_000");
        assertParseFloat(null, "1000_000.0");
        assertParseFloat(null, "1_000_00.0");
        assertParseFloat(null, "0.00_1");
        assertParseFloat(null, "0.0000_1");
        assertParseFloat(null, "1T");
        assertParseFloat(null, "1_T");
        assertParseFloat(null, "1_m");
        assertParseFloat(null, "1.");
        assertParseFloat(null, ".1");
        assertParseFloat(null, ".1e1");
        assertParseFloat(null, "1.e1");
        assertParseFloat(null, "1.-1");
        assertParseFloat(null, "1.1-");
        assertParseFloat(null, "1-.1");
        assertParseFloat(null, "1.1.1");
        assertParseFloat(null, "1.0e1.1");
        assertParseFloat(null, "1.0e1e1");
        assertParseFloat(null, "1.0e1k");
        assertParseFloat(null, "1.0kk");
    }
    
    @Test
    public void testConversionToInteger() {
        assertEquals(0, Float.instance(0.5).getInteger());
        assertEquals(0, Float.instance(-0.0).getInteger());
        assertEquals(0, Float.instance(-0.5).getInteger());
        assertEquals(Long.MAX_VALUE, Float.instance(Long.MAX_VALUE).getInteger());
        assertEquals(Long.MIN_VALUE, Float.instance(Long.MIN_VALUE).getInteger());
        
        try {
            Float.instance(Math.nextUp(((double)Long.MAX_VALUE))).getInteger();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Float.instance(Math.nextAfter((((double)Long.MIN_VALUE)), Double.NEGATIVE_INFINITY)).getInteger();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Float.instance(Double.POSITIVE_INFINITY).getInteger();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Float.instance(Double.NEGATIVE_INFINITY).getInteger();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
        
        try {
            Float.instance(Double.NaN).getInteger();
            fail("OverflowException expected");
        } catch (OverflowException e) {
            // Checking that this is thrown
        }
    }

}

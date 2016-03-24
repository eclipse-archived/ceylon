package ceylon.language;

import static ceylon.language.parseFloat_.parseFloat;
import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
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
    public void testFloatFractionalPart() {
        assertEquals(-0.0, Float.getFractionalPart(-0.0), 0.0);
        assertEquals(+0.0, Float.getFractionalPart(+0.0), 0.0);
        assertEquals(+0.4, Float.getFractionalPart(+0.4), 0.0000001);
        assertEquals(-0.4, Float.getFractionalPart(-0.4), 0.0000001);
        assertEquals(+0.6, Float.getFractionalPart(+0.6), 0.0000001);
        assertEquals(-0.6, Float.getFractionalPart(-0.6), 0.0000001);
        assertEquals(-0.0, Float.getFractionalPart(-1.0), 0.0000001);
        assertEquals(+0.0, Float.getFractionalPart(+1.0), 0.0000001);
        assertEquals(+0.4, Float.getFractionalPart(+1.4), 0.0000001);
        assertEquals(-0.4, Float.getFractionalPart(-1.4), 0.0000001);
        assertEquals(+0.6, Float.getFractionalPart(+1.6), 0.0000001);
        assertEquals(-0.6, Float.getFractionalPart(-1.6), 0.0000001);

        assertEquals(+0.5d, Float.getFractionalPart(+2097153.5d), 0.0);
        assertEquals(-0.5d, Float.getFractionalPart(-2097153.5d), 0.0);

        assertEquals(0.0, Float.getFractionalPart((double) +(2L << 52) + 0.5d), 0.0);
        assertEquals(0.0, Float.getFractionalPart((double) -(2L << 52) - 0.5d), 0.0);

        assertEquals(0.0, Float.getFractionalPart((double) +(2L << 53) + 0.5d), 0.0);
        assertEquals(0.0, Float.getFractionalPart((double) -(2L << 53) - 0.5d), 0.0);

        assertTrue("preserve negative 0", 1 / Float.getFractionalPart(-0.0) < 0);
        assertTrue("preserve positive 0", 1 / Float.getFractionalPart(+0.0) > 0);
        assertTrue("preserve negative 0", 1 / Float.getFractionalPart(-0.4) < 0);
        assertTrue("preserve positive 0", 1 / Float.getFractionalPart(+0.4) > 0);
        assertTrue("preserve negative 0", 1 / Float.getFractionalPart(-0.6) < 0);
        assertTrue("preserve positive 0", 1 / Float.getFractionalPart(+0.6) > 0);

        assertTrue("preserve negative 0", 1 / Float.getFractionalPart(NEGATIVE_INFINITY) < 0);
        assertTrue("preserve positive 0", 1 / Float.getFractionalPart(POSITIVE_INFINITY) > 0);
        assertEquals(Float.getFractionalPart(NaN), NaN, 0.0);
    }

    @Test
    public void testFloatFractionalPartInstance() {
        assertEquals(-0.0, Float.instance(-0.0).getFractionalPart().value, 0.0);
        assertEquals(+0.0, Float.instance(+0.0).getFractionalPart().value, 0.0);
        assertEquals(+0.4, Float.instance(+0.4).getFractionalPart().value, 0.0000001);
        assertEquals(-0.4, Float.instance(-0.4).getFractionalPart().value, 0.0000001);
        assertEquals(+0.6, Float.instance(+0.6).getFractionalPart().value, 0.0000001);
        assertEquals(-0.6, Float.instance(-0.6).getFractionalPart().value, 0.0000001);
        assertEquals(-0.0, Float.instance(-1.0).getFractionalPart().value, 0.0000001);
        assertEquals(+0.0, Float.instance(+1.0).getFractionalPart().value, 0.0000001);
        assertEquals(+0.4, Float.instance(+1.4).getFractionalPart().value, 0.0000001);
        assertEquals(-0.4, Float.instance(-1.4).getFractionalPart().value, 0.0000001);
        assertEquals(+0.6, Float.instance(+1.6).getFractionalPart().value, 0.0000001);
        assertEquals(-0.6, Float.instance(-1.6).getFractionalPart().value, 0.0000001);

        assertEquals(+0.5d, Float.instance(+2097153.5d).getFractionalPart().value, 0.0);
        assertEquals(-0.5d, Float.instance(-2097153.5d).getFractionalPart().value, 0.0);

        assertEquals(0.0, Float.instance((double) +(2L << 52) + 0.5d).getFractionalPart().value, 0.0);
        assertEquals(0.0, Float.instance((double) -(2L << 52) - 0.5d).getFractionalPart().value, 0.0);

        assertEquals(0.0, Float.instance((double) +(2L << 53) + 0.5d).getFractionalPart().value, 0.0);
        assertEquals(0.0, Float.instance((double) -(2L << 53) - 0.5d).getFractionalPart().value, 0.0);

        assertTrue("preserve negative 0", 1 / Float.instance(-0.0).getFractionalPart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.0).getFractionalPart().value > 0);
        assertTrue("preserve negative 0", 1 / Float.instance(-0.4).getFractionalPart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.4).getFractionalPart().value > 0);
        assertTrue("preserve negative 0", 1 / Float.instance(-0.6).getFractionalPart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.6).getFractionalPart().value > 0);

        assertTrue("preserve negative 0", 1 / Float.instance(NEGATIVE_INFINITY).getFractionalPart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(POSITIVE_INFINITY).getFractionalPart().value > 0);
        assertEquals(Float.getFractionalPart(NaN), NaN, 0.0);
    }

    @Test
    public void testFloatWholePart() {
        assertEquals(0.0, Float.getWholePart(-0.0), 0.0);
        assertEquals(0.0, Float.getWholePart(+0.0), 0.0);
        assertEquals(0.0, Float.getWholePart(+0.4), 0.0);
        assertEquals(0.0, Float.getWholePart(-0.4), 0.0);
        assertEquals(0.0, Float.getWholePart(+0.6), 0.0);
        assertEquals(0.0, Float.getWholePart(-0.6), 0.0);
        assertEquals(-1.0, Float.getWholePart(-1.0), 0.0);
        assertEquals(+1.0, Float.getWholePart(+1.0), 0.0);
        assertEquals(+1.0, Float.getWholePart(+1.4), 0.0);
        assertEquals(-1.0, Float.getWholePart(-1.4), 0.0);
        assertEquals(+1.0, Float.getWholePart(+1.6), 0.0);
        assertEquals(-1.0, Float.getWholePart(-1.6), 0.0);

        assertEquals(+2097153d, Float.getWholePart(+2097153.5d), 0.0);
        assertEquals(-2097153d, Float.getWholePart(-2097153.5d), 0.0);

        assertEquals((double) +(2L << 52), Float.getWholePart((double) +(2L << 52) + 0.5d), 0.0);
        assertEquals((double) -(2L << 52), Float.getWholePart((double) -(2L << 52) - 0.5d), 0.0);

        assertEquals((double) +(2L << 53), Float.getWholePart((double) +(2L << 53) + 0.5d), 0.0);
        assertEquals((double) -(2L << 53), Float.getWholePart((double) -(2L << 53) - 0.5d), 0.0);

        assertTrue("preserve negative 0", 1 / Float.getWholePart(-0.0) < 0);
        assertTrue("preserve positive 0", 1 / Float.getWholePart(+0.0) > 0);
        assertTrue("preserve negative 0", 1 / Float.getWholePart(-0.4) < 0);
        assertTrue("preserve positive 0", 1 / Float.getWholePart(+0.4) > 0);
        assertTrue("preserve negative 0", 1 / Float.getWholePart(-0.6) < 0);
        assertTrue("preserve positive 0", 1 / Float.getWholePart(+0.6) > 0);

        assertEquals(Float.getWholePart(NEGATIVE_INFINITY), NEGATIVE_INFINITY, 0.0);
        assertEquals(Float.getWholePart(POSITIVE_INFINITY), POSITIVE_INFINITY, 0.0);
        assertEquals(Float.getWholePart(NaN), NaN, 0.0);
    }

    @Test
    public void testFloatWholePartInstance() {
        assertEquals(0.0, Float.instance(-0.0).getWholePart().value, 0.0);
        assertEquals(0.0, Float.instance(+0.0).getWholePart().value, 0.0);
        assertEquals(0.0, Float.instance(+0.4).getWholePart().value, 0.0);
        assertEquals(0.0, Float.instance(-0.4).getWholePart().value, 0.0);
        assertEquals(0.0, Float.instance(+0.6).getWholePart().value, 0.0);
        assertEquals(0.0, Float.instance(-0.6).getWholePart().value, 0.0);
        assertEquals(-1.0, Float.instance(-1.0).getWholePart().value, 0.0);
        assertEquals(+1.0, Float.instance(+1.0).getWholePart().value, 0.0);
        assertEquals(+1.0, Float.instance(+1.4).getWholePart().value, 0.0);
        assertEquals(-1.0, Float.instance(-1.4).getWholePart().value, 0.0);
        assertEquals(+1.0, Float.instance(+1.6).getWholePart().value, 0.0);
        assertEquals(-1.0, Float.instance(-1.6).getWholePart().value, 0.0);

        assertEquals(+2097153d, Float.instance(+2097153.5d).getWholePart().value, 0.0);
        assertEquals(-2097153d, Float.instance(-2097153.5d).getWholePart().value, 0.0);

        assertEquals((double) +(2L << 52), Float.instance((double) +(2L << 52) + 0.5d).getWholePart().value, 0.0);
        assertEquals((double) -(2L << 52), Float.instance((double) -(2L << 52) - 0.5d).getWholePart().value, 0.0);

        assertEquals((double) +(2L << 53), Float.instance((double) +(2L << 53) + 0.5d).getWholePart().value, 0.0);
        assertEquals((double) -(2L << 53), Float.instance((double) -(2L << 53) - 0.5d).getWholePart().value, 0.0);

        assertTrue("preserve negative 0", 1 / Float.instance(-0.0).getWholePart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.0).getWholePart().value > 0);
        assertTrue("preserve negative 0", 1 / Float.instance(-0.4).getWholePart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.4).getWholePart().value > 0);
        assertTrue("preserve negative 0", 1 / Float.instance(-0.6).getWholePart().value < 0);
        assertTrue("preserve positive 0", 1 / Float.instance(+0.6).getWholePart().value > 0);

        assertEquals(Float.instance(NEGATIVE_INFINITY).getWholePart().value, NEGATIVE_INFINITY, 0.0);
        assertEquals(Float.instance(POSITIVE_INFINITY).getWholePart().value, POSITIVE_INFINITY, 0.0);
        assertEquals(Float.instance(NaN).getWholePart().value, NaN, 0.0);
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
        assertParseFloat(1e-6, "0.000001");
        assertParseFloat(1e-9, "0.000000001");
        
        assertParseFloat(1e3, "+1.0E+3");
        
        assertParseFloat(1e3, "+1.000000E+3");
        assertParseFloat(1e3, "+1000.0");
        assertParseFloat(1e6, "+1000000.0");
        assertParseFloat(1e6, "+1000000.000000000");
        assertParseFloat(1e6, "+1000000.000000000e000000");
        
        assertParseFloat(Double.POSITIVE_INFINITY, "+1.0E+1000");
        
        assertParseFloat(1.0, "1.0000");
        
        assertParseFloat(1e+3, "1E+3");
        assertParseFloat(1e+3, "1e+3");
        assertParseFloat(1e+100, "1e+100");
        assertParseFloat(null, "1_000_000.0");
        assertParseFloat(null, "100_000.0");
        assertParseFloat(null, "0.0_01");
        assertParseFloat(null, "0.000_01");
        assertParseFloat(1.0e12, "1T");
        assertParseFloat(null, "1_T");
        assertParseFloat(null, "1_m");
        assertParseFloat(1.0, "1.");
        assertParseFloat(0.1, ".1");
        assertParseFloat(1.0, ".1e1");
        assertParseFloat(10.0, "1.e1");
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

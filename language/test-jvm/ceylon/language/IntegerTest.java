package ceylon.language;

import static ceylon.language.parseInteger_.parseInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class IntegerTest {

	private void assertParseInteger(Long expect, java.lang.String str) {
		Integer parsed = parseInteger(str);
		if (expect != null) {
			if (parsed == null) {
				fail(str + " didn't parse as an Integer");
			}
			assertEquals("parseInteger(" + str + ")" + parsed.longValue(), 
					expect.longValue(), parsed.longValue());
		} else {
			assertNull("parseInteger(" + str + ") returned a result " + parsed, parsed);
		}
	}
	
	@Test
	public void testParseInteger() {
		assertParseInteger(0L, "0");
		assertParseInteger(1000L, "1_000");
		assertParseInteger(1000L, "1000");
		assertParseInteger(1000L, "1k");
		assertParseInteger(1000L, "+1_000");
		assertParseInteger(1000L, "+1000");
		assertParseInteger(1000L, "+1k");
		assertParseInteger(-1000L, "-1_000");
		assertParseInteger(-1000L, "-1000");
		assertParseInteger(-1000L, "-1k");
		
		assertParseInteger(0L, "0");
		assertParseInteger(0L, "00");
		assertParseInteger(0L, "0_000");
		assertParseInteger(0L, "-00");
		assertParseInteger(0L, "+00");
		assertParseInteger(0L, "0k");
		
		assertParseInteger(1L, "1");
		assertParseInteger(1L, "01");
		assertParseInteger(1L, "0_001");
		assertParseInteger(1L, "+1");
		assertParseInteger(1L, "+01");
		assertParseInteger(1L, "+0_001");
		
		assertParseInteger(-1L, "-1");
		assertParseInteger(-1L, "-01");
		assertParseInteger(-1L, "-0_001");
		
		assertParseInteger(1000L, "1k");
		assertParseInteger(1000000L, "1M");
		assertParseInteger(1000000000L, "1G");
		assertParseInteger(1000000000000L, "1T");
		assertParseInteger(1000000000000000L, "1P");
		assertParseInteger(-1000L, "-1k");
		assertParseInteger(-1000000L, "-1M");
		assertParseInteger(-1000000000L, "-1G");
		assertParseInteger(-1000000000000L, "-1T");
		assertParseInteger(-1000000000000000L, "-1P");
		
		assertParseInteger(9223372036854775807L, "9223372036854775807");
		assertParseInteger(9223372036854775807L, "9_223_372_036_854_775_807");
		assertParseInteger(-9223372036854775808L, "-9223372036854775808");
		assertParseInteger(-9223372036854775808L, "-9_223_372_036_854_775_808");
		
		
		assertParseInteger(null, "");
		assertParseInteger(null, "_");
		assertParseInteger(null, "+");
		assertParseInteger(null, "+_");
		assertParseInteger(null, "-");
		assertParseInteger(null, "-_");
		assertParseInteger(null, "foo");
		assertParseInteger(null, " 0");
		assertParseInteger(null, "0 ");
		assertParseInteger(null, "0+0");
		assertParseInteger(null, "0-0");
		assertParseInteger(null, "0+");
		assertParseInteger(null, "0-");
		assertParseInteger(null, "k");
		assertParseInteger(null, "k1");
		assertParseInteger(null, "+k");
		assertParseInteger(null, "-k");
		assertParseInteger(null, "1kk");
		assertParseInteger(null, "0_");
		assertParseInteger(null, "_0");
		assertParseInteger(null, "0_0");
		assertParseInteger(null, "0_00");
		assertParseInteger(null, "0_0000");
		assertParseInteger(null, "0_000_0");
		assertParseInteger(null, "0000_000");
		assertParseInteger(null, "1_0");
		assertParseInteger(null, "!_00");
		assertParseInteger(null, "1_0000");
		assertParseInteger(null, "1_000_0");
		assertParseInteger(null, "1000_000");
		assertParseInteger(null, "9223372036854775808");
		assertParseInteger(null, "-9223372036854775809");
	}
	
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

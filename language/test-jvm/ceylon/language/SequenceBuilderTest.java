package ceylon.language;

import org.junit.Assert;
import org.junit.Test;

public class SequenceBuilderTest {

    private static ceylon.language.Integer i(int i) {
        return ceylon.language.Integer.instance(i);
    }
    
    private static ceylon.language.String s(java.lang.String s) {
        return ceylon.language.String.instance(s);
    }
    
    private static ceylon.language.String[] cs(java.lang.String... jstrings) {
        ceylon.language.String[] result = new ceylon.language.String[jstrings.length];
        for (int ii = 0; ii < jstrings.length; ii++) {
            result[ii] = ceylon.language.String.instance(jstrings[ii]);
        }
        return result;
    }
    
    @SuppressWarnings("rawtypes")
    private final Sequential empty = empty_.get_();
    
    @Test
    public void testEmpty() {
        SequenceBuilder<String> sb = new SequenceBuilder<String>(String.$TypeDescriptor);
        Assert.assertEquals(0, sb.getSize());
        Assert.assertTrue(sb.getEmpty());
        Assert.assertEquals(empty, sb.getSequence());
    }
    @Test
    public void test123() {
        SequenceBuilder<String> sb = new SequenceBuilder<String>(String.$TypeDescriptor);
        sb.append(s("1"));
        sb.append(s("2"));
        sb.append(s("3"));
        Assert.assertEquals(3, sb.getSize());
        Assert.assertFalse(sb.getEmpty());
        Assert.assertEquals("[1, 2, 3]", sb.getSequence().toString());
        
        sb.appendAll(empty);
        Assert.assertEquals(3, sb.getSize());
        Assert.assertFalse(sb.getEmpty());
        Assert.assertEquals("[1, 2, 3]", sb.getSequence().toString());
        
        sb.appendAll(sb.getSequence());
        Assert.assertEquals(6, sb.getSize());
        Assert.assertFalse(sb.getEmpty());
        Assert.assertEquals("[1, 2, 3, 1, 2, 3]", sb.getSequence().toString());
    }
    @Test
    public void test1000() {
        SequenceBuilder<String> sb = new SequenceBuilder<String>(String.$TypeDescriptor);
        for (int ii = 0; ii < 1000; ii++) {
            sb.append(s(""+ii));
        }
        Assert.assertEquals(1000, sb.getSize());
        Assert.assertFalse(sb.getEmpty());
        java.lang.String str = sb.getSequence().toString();
        Assert.assertTrue(str.substring(0, 10), str.startsWith("[0, 1, 2, "));
        Assert.assertTrue(str.endsWith(", 998, 999]"));
    }

}

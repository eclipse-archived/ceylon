package ceylon.language;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

import ceylon.language.ArraySequence;
import ceylon.language.Boolean;
import ceylon.language.Iterator;
import ceylon.language.Sequential;
import ceylon.language.empty_;
import ceylon.language.finished_;

public class ArraySequenceTest {

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
    public void testConstructor() {
        try {
            ArraySequence.instance(ceylon.language.String.$TypeDescriptor, 
                    cs());
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // checking this is thrown
        }
        
        try {
            ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                    cs("a"), 1, 1);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // checking this is thrown
        }
        
        try {
            ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                    cs("a"), 0, 0);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // checking this is thrown
        }
        
        
        ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                cs("a"), 0, 1);
        
        try {
            ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                    cs("a"), 0, 2);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // checking this is thrown
        }
            
        
    }
    
    /**
     * Returns a list of {@link ArraySequence}s representing 
     * <code>{ "a", "b", "c" }</code>, but each has a different backing 
     * array.
     */
    Map<java.lang.String, ArraySequence<ceylon.language.String>> abcs() {
        Map<java.lang.String, ArraySequence<ceylon.language.String>> result = new LinkedHashMap<java.lang.String, ArraySequence<ceylon.language.String>>();
        result.put("{a b c}: ", 
                ArraySequence.<String>instance(
                        ceylon.language.String.$TypeDescriptor, 
                        cs("a", "b", "c")));
        result.put("{a b c d}, 0, 3: ", 
                ArraySequence.backedBy$hidden(
                        ceylon.language.String.$TypeDescriptor, 
                        cs("a", "b", "c", "d"), 0, 3));
        result.put("{z a b c}, 1, 3: ", 
                ArraySequence.backedBy$hidden(
                        ceylon.language.String.$TypeDescriptor, 
                        cs("z", "a", "b", "c"), 1, 3));
        result.put("{z a b c d}, 1, 3: ", 
                ArraySequence.backedBy$hidden(
                        ceylon.language.String.$TypeDescriptor, 
                        cs("z", "a", "b", "c", "d"), 1, 3));
        return result;
    }
    
    /**
     * This test is mainly aimed as checking ArraySequence
     * accesses the elements of the array correctly, respecting the
     * first and length parameters passed to it's ctor.
     */    
    @Test
    public void testBasic() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            Assert.assertFalse(description, abc1.getEmpty());
            Assert.assertEquals(description, abc1.getSize(), 3);
            Assert.assertEquals(description, abc1.getFirst().value, "a");
            Assert.assertEquals(description, abc1.getLast().value, "c");
            Assert.assertEquals(description, abc1.getLastIndex().longValue(), 2L);
        }
    }
    @Test
    public void testDefines() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            Assert.assertTrue(description, abc1.defines(i(0)));
            Assert.assertTrue(description, abc1.defines(i(1)));
            Assert.assertTrue(description, abc1.defines(i(2)));
            Assert.assertFalse(description, abc1.defines(i(3)));
        }
    }
            
    @Test
    public void testContains() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            Assert.assertTrue(description, abc1.contains(s("a")));
            Assert.assertTrue(description, abc1.contains(s("b")));
            Assert.assertTrue(description, abc1.contains(s("c")));
            Assert.assertFalse(description, abc1.contains(s("d")));
            Assert.assertFalse(description, abc1.contains(s("z")));
        }
    }
    
    @Test
    public void testGet() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            Assert.assertEquals(description, s("a"), abc1.get(i(0)));
            Assert.assertEquals(description, s("b"), abc1.get(i(1)));
            Assert.assertEquals(description, s("c"), abc1.get(i(2)));
            Assert.assertEquals(description, null, abc1.get(i(3)));
        }
    }
    
    @Test
    public void testRest() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            final ArraySequence<ceylon.language.String> bc = 
                    ArraySequence.instance(ceylon.language.String.$TypeDescriptor, 
                    cs("b", "c"));
            Assert.assertEquals(description, abc1.getRest(), bc);
        }
    }
    
    @Test
    public void testCount() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            
            Assert.assertEquals(description, 1, abc1.count(equalsPredicate("a")));
            Assert.assertEquals(description, 1, abc1.count(equalsPredicate("b")));
            Assert.assertEquals(description, 1, abc1.count(equalsPredicate("c")));
            Assert.assertEquals(description, 0, abc1.count(equalsPredicate("d")));
            Assert.assertEquals(description, 0, abc1.count(equalsPredicate("z")));
        }
    }
    
    private AbstractCallable<Boolean> equalsPredicate(java.lang.String str) {
        final ceylon.language.String toMatch = s(str);
        return new AbstractCallable<ceylon.language.Boolean>(Boolean.$TypeDescriptor, 
                TypeDescriptor.klass(Tuple.class, String.$TypeDescriptor, String.$TypeDescriptor, Empty.$TypeDescriptor),
                "", (short)-1) {
            public ceylon.language.Boolean $call(java.lang.Object arg0) {
                ceylon.language.String argString = (ceylon.language.String)arg0;
                return ceylon.language.Boolean.instance(toMatch.equals(argString));
            }
        };
    }
    
    @Test
    public void testSort() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            ArraySequence<ceylon.language.String> cba = ArraySequence.instance(ceylon.language.String.$TypeDescriptor, 
                    cs("c", "b", "a"));
            AbstractCallable<ceylon.language.Comparison> alphabeticOrder = alphabeticalOrder();
            Assert.assertEquals(description, abc1, abc1.sort(alphabeticOrder));
            Assert.assertEquals(description, abc1, cba.sort(alphabeticOrder));
        }
    }
    
    private AbstractCallable<ceylon.language.Comparison> alphabeticalOrder() {
        AbstractCallable<ceylon.language.Comparison> alphabeticOrder = new AbstractCallable<ceylon.language.Comparison>(Comparison.$TypeDescriptor, 
                TypeDescriptor.klass(Tuple.class, String.$TypeDescriptor, String.$TypeDescriptor, TypeDescriptor.klass(Tuple.class, String.$TypeDescriptor, String.$TypeDescriptor, Empty.$TypeDescriptor)),
                "", (short)-1) {
            public ceylon.language.Comparison $call(java.lang.Object arg0, java.lang.Object arg1) {
                ceylon.language.String first = (ceylon.language.String)arg0;
                ceylon.language.String second = (ceylon.language.String)arg1;
                return first.compare(second);
            }
        };
        return alphabeticOrder;
    }
    
    @Test
    public void testIterator() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abc : abcs().entrySet()) {
            java.lang.String description = abc.getKey();
            ArraySequence<ceylon.language.String> abc1 = abc.getValue();
            Iterator<ceylon.language.String> iterator = abc1.iterator();
            Assert.assertEquals(description, s("a"), iterator.next());
            Assert.assertEquals(description, s("b"), iterator.next());
            Assert.assertEquals(description, s("c"), iterator.next());
            Assert.assertEquals(description, finished_.get_(), iterator.next());
        }
    }

    @Test
    public void testReversed() {
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abcs : abcs().entrySet()) {
            java.lang.String description = abcs.getKey();
            ArraySequence<ceylon.language.String> abc = abcs.getValue();
        
            Assert.assertEquals(description, abc.getReversed().toString(), "[c, b, a]");
            
            ArraySequence<ceylon.language.String> bc = ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                    cs("a", "b", "c"), 1, 2);
            Assert.assertEquals(description, bc.getReversed().toString(), "[c, b]");
            
            ArraySequence<ceylon.language.String> b = ArraySequence.backedBy$hidden(ceylon.language.String.$TypeDescriptor, 
                    cs("a", "b", "c"), 1, 1);
            Assert.assertEquals(description, b.getReversed().toString(), "[b]");
        }
    }
    
    
    @Test
    public void testSpan() {
        ArraySequence<ceylon.language.String> ab = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("a", "b"));
        ArraySequence<ceylon.language.String> a = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("a"));
        ArraySequence<ceylon.language.String> bc = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("b","c"));
        ArraySequence<ceylon.language.String> c = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("c"));
        
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abcs : abcs().entrySet()) {
            java.lang.String description = abcs.getKey();
            ArraySequence<ceylon.language.String> abc = abcs.getValue();
            
            Assert.assertEquals(description, a,   abc.span(i(0), i(0)));
            Assert.assertEquals(description, ab,  abc.span(i(0), i(1)));
            Assert.assertEquals(description, abc, abc.span(i(0), i(2)));
            Assert.assertEquals(description, abc, abc.span(i(0), i(3)));
            
            Assert.assertEquals(description, bc, abc.span(i(1), i(2)));
            Assert.assertEquals(description, bc, abc.span(i(1), i(4)));
            
            Assert.assertEquals(description, c, abc.span(i(2), i(2)));
            Assert.assertEquals(description, c, abc.span(i(2), i(3)));
            Assert.assertEquals(description, empty, abc.span(i(3), i(3)));
            
            Assert.assertEquals(description, a,   abc.span(i(-1), i(0)));
            Assert.assertEquals(description, abc, abc.span(i(-1), i(3)));
            
            // Reversed
            Assert.assertEquals(description, a, abc.span(i(0), i(0)));
            Assert.assertEquals(description, ab.getReversed(),  abc.span(i(1), i(0)));
            Assert.assertEquals(description, abc.getReversed(), abc.span(i(2), i(0)));
            Assert.assertEquals(description, abc.getReversed(), abc.span(i(3), i(0)));
            
            Assert.assertEquals(description, bc.getReversed(), abc.span(i(2), i(1)));
            Assert.assertEquals(description, bc.getReversed(), abc.span(i(4), i(1)));
            
            Assert.assertEquals(description, c, abc.span(i(2), i(2)));
            Assert.assertEquals(description, c, abc.span(i(3), i(2)));
            Assert.assertEquals(description, empty, abc.span(i(3), i(3)));
            
            Assert.assertEquals(description, a,   abc.span(i(0), i(-1)));
            Assert.assertEquals(description, abc.getReversed(),   abc.span(i(3), i(-1)));
        }
    }
    
    @Test
    public void testSpanFrom() {
        ArraySequence<ceylon.language.String> bc = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("b","c"));
        ArraySequence<ceylon.language.String> c = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("c"));
        
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abcs : abcs().entrySet()) {
            java.lang.String description = abcs.getKey();
            ArraySequence<ceylon.language.String> abc = abcs.getValue();
            Assert.assertEquals(description, abc, abc.spanFrom(i(-1)));
            Assert.assertEquals(description, abc, abc.spanFrom(i(0)));
            Assert.assertEquals(description, bc,  abc.spanFrom(i(1)));
            Assert.assertEquals(description, c,   abc.spanFrom(i(2)));
            Assert.assertEquals(description, empty, abc.spanFrom(i(3)));   
        }
    }
    
    @Test
    public void testSpanTo() {
        ArraySequence<ceylon.language.String> ab = ArraySequence.instance(ceylon.language.String.$TypeDescriptor, 
                cs("a", "b"));
        ArraySequence<ceylon.language.String> a = ArraySequence.instance(ceylon.language.String.$TypeDescriptor, 
                cs("a"));
        
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abcs : abcs().entrySet()) {
            java.lang.String description = abcs.getKey();
            ArraySequence<ceylon.language.String> abc = abcs.getValue();
            
            Assert.assertEquals(description, abc, abc.spanTo(i(3)));
            Assert.assertEquals(description, abc,   abc.spanTo(i(2)));
            Assert.assertEquals(description, ab,  abc.spanTo(i(1)));
            Assert.assertEquals(description, a, abc.spanTo(i(0)));
            Assert.assertEquals(description, empty, abc.spanTo(i(-1)));
        }
    }
    
    @Test
    public void testSegment() {
        ArraySequence<ceylon.language.String> ab = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("a", "b"));
        ArraySequence<ceylon.language.String> a = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("a"));
        ArraySequence<ceylon.language.String> b = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("b"));
        ArraySequence<ceylon.language.String> bc = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("b","c"));
        ArraySequence<ceylon.language.String> c = ArraySequence.instance(
                ceylon.language.String.$TypeDescriptor, 
                cs("c"));
        for (Map.Entry<java.lang.String, ArraySequence<ceylon.language.String>> abcs : abcs().entrySet()) {
            java.lang.String description = abcs.getKey();
            ArraySequence<ceylon.language.String> abc = abcs.getValue();
        
            Assert.assertEquals(description, abc,   abc.segment(i(-1), 4));
            Assert.assertEquals(description, ab,    abc.segment(i(-1), 3));
            Assert.assertEquals(description, a,     abc.segment(i(-1), 2));
            Assert.assertEquals(description, empty, abc.segment(i(-1), 1));
            Assert.assertEquals(description, empty, abc.segment(i(-1), 0));
            
            Assert.assertEquals(description, abc,   abc.segment(i(0), 4));
            Assert.assertEquals(description, abc,   abc.segment(i(0), 3));
            Assert.assertEquals(description, ab,    abc.segment(i(0), 2));
            Assert.assertEquals(description, a,     abc.segment(i(0), 1));
            Assert.assertEquals(description, empty, abc.segment(i(0), 0));
            
            Assert.assertEquals(description, bc,    abc.segment(i(1), 4));
            Assert.assertEquals(description, bc,    abc.segment(i(1), 3));
            Assert.assertEquals(description, bc,    abc.segment(i(1), 2));
            Assert.assertEquals(description, b,     abc.segment(i(1), 1));
            Assert.assertEquals(description, empty, abc.segment(i(1), 0));
            
            Assert.assertEquals(description, c,     abc.segment(i(2), 4));
            Assert.assertEquals(description, c,     abc.segment(i(2), 3));
            Assert.assertEquals(description, c,     abc.segment(i(2), 2));
            Assert.assertEquals(description, c,     abc.segment(i(2), 1));
            Assert.assertEquals(description, empty, abc.segment(i(2), 0));
            
            Assert.assertEquals(description, empty, abc.segment(i(3), 4));
            Assert.assertEquals(description, empty, abc.segment(i(3), 3));
            Assert.assertEquals(description, empty, abc.segment(i(3), 2));
            Assert.assertEquals(description, empty, abc.segment(i(3), 1));
            Assert.assertEquals(description, empty, abc.segment(i(3), 0));
        }
    }

    
}

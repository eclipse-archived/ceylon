package com.redhat.ceylon.compiler.java;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;
import com.redhat.ceylon.compiler.java.wrapping.Wrapping;
import com.redhat.ceylon.compiler.java.wrapping.Wrappings;

import ceylon.language.Finished;
import ceylon.language.Null;

public class WrappingTest {

    Object getNull() {
        return null;
    }
    
    @Test
    public void testToCeylonSetString() {
        java.util.Set<String> set = new java.util.HashSet<String>();
        set.add("foo");
        set.add("bar");
        set.add("baz");
        
        Wrapping<java.util.Set<String>, ceylon.language.Set<ceylon.language.String>> wrapping = Wrappings.toCeylonSet(ceylon.language.String.$TypeDescriptor$, false);
        ceylon.language.Set<ceylon.language.String> cSet = wrapping.wrap(set);
        
        assertTrue(cSet.contains(ceylon.language.String.instance("foo")));
        assertTrue(cSet.contains(ceylon.language.String.instance("bar")));
        assertTrue(cSet.contains(ceylon.language.String.instance("baz")));
        assertFalse(cSet.contains(ceylon.language.String.instance("gee")));
        assertFalse(cSet.contains(getNull()));
        
        java.util.Set<String> set2 = wrapping.inverted().wrap(cSet);
        
        assertTrue(set == set2);
        
        // Try to access a set wrapping a set containing null
        java.util.Set<String> setWithNull = new java.util.HashSet<String>();
        setWithNull.add(null);
        ceylon.language.Set<ceylon.language.String> cSetThrowOnNull = wrapping.wrap(setWithNull);
        try {
            cSetThrowOnNull.iterator().next();
        } catch (ceylon.language.AssertionError e) {
            assertEquals("null value present in wrapping of java.lang.String into ceylon.language.String", e.getMessage());
        }
        
        // Try to wrap a null set
        try {
            wrapping.wrap(null);
        } catch (ceylon.language.AssertionError e) {
            assertEquals("null value present in wrapping of java.util.Set into ceylon.language.Set", e.getMessage());
        }
        Wrapping<java.util.Set<String>, ceylon.language.Set<ceylon.language.String>> nullWrapping = Wrappings.toCeylonSet(ceylon.language.String.$TypeDescriptor$, true);
        assertEquals(null, nullWrapping.wrap(null));
        
        Wrapping<java.util.Set<String>, ceylon.language.Set<ceylon.language.String>> nullableWrapping = Wrappings.toCeylonSet(TypeDescriptor.union(ceylon.language.String.$TypeDescriptor$, Null.$TypeDescriptor$), false);
        ceylon.language.Set<ceylon.language.String> cSetNullAllowed = nullableWrapping.wrap(setWithNull);
        assertEquals(null, cSetNullAllowed.iterator().next());
    }
    
    @Test
    public void testToCeylonListString() {
        java.util.List<String> list = new java.util.ArrayList<String>();
        list.add("foo");
        list.add("bar");
        list.add("baz");
        
        Wrapping<java.util.List<String>, ceylon.language.List<ceylon.language.String>> wrapping = Wrappings.toCeylonList(ceylon.language.String.$TypeDescriptor$, false);
        ceylon.language.List<ceylon.language.String> cList = wrapping.wrap(list);
        
        assertTrue(cList.contains(ceylon.language.String.instance("foo")));
        assertTrue(cList.contains(ceylon.language.String.instance("bar")));
        assertTrue(cList.contains(ceylon.language.String.instance("baz")));
        assertFalse(cList.contains(ceylon.language.String.instance("gee")));
        assertFalse(cList.contains(getNull()));
        
        java.util.List<String> list2 = wrapping.inverted().wrap(cList);
        
        assertTrue(list == list2);
        
        // Now wrap a list containing null
        list = new java.util.ArrayList<String>();
        list.add(null);
        cList = wrapping.wrap(list);
        try {
            cList.iterator().next();
        } catch (ceylon.language.AssertionError e) {
            assertEquals("null value present in wrapping of java.lang.String into ceylon.language.String", e.getMessage());
        }
    }
    
    @Test
    public void testToCeylonMapString() {
        java.util.Map<String,Long> map = new java.util.HashMap<String,Long>();
        map.put("foo", 1L);
        map.put("bar", 2L);
        map.put("baz", 3L);
        
        Wrapping<java.util.Map<String,Long>, ceylon.language.Map<ceylon.language.String,ceylon.language.Integer>> wrapping = Wrappings.toCeylonMap(
                ceylon.language.String.$TypeDescriptor$,
                ceylon.language.Integer.$TypeDescriptor$, false);
        ceylon.language.Map<ceylon.language.String,ceylon.language.Integer> cMap = wrapping.wrap(map);
        
        assertEquals(ceylon.language.Integer.instance(1), cMap.get(ceylon.language.String.instance("foo")));
        assertEquals(ceylon.language.Integer.instance(2), cMap.get(ceylon.language.String.instance("bar")));
        assertEquals(ceylon.language.Integer.instance(3), cMap.get(ceylon.language.String.instance("baz")));
        assertEquals(null, cMap.get(ceylon.language.String.instance("gee")));
        
        java.util.Map<String,Long> map2 = wrapping.inverted().wrap(cMap);
        
        assertTrue(map == map2);
    }
    
    @Test
    public void testToCeylonListListString() {
        java.util.List<java.util.List<String>> list = new java.util.ArrayList<java.util.List<String>>();
        list.add(Arrays.asList("foo", "bar"));
        list.add(Arrays.asList("bar", "baz"));
        
        Wrapping<java.util.List<java.util.List<String>>, ceylon.language.List<ceylon.language.List<ceylon.language.String>>> wrapping = Wrappings.toCeylonList(
                TypeDescriptor.klass(ceylon.language.List.class, ceylon.language.String.$TypeDescriptor$), false);
        ceylon.language.List<ceylon.language.List<ceylon.language.String>> cList = wrapping.wrap(list);
        
        ceylon.language.Iterator<? extends ceylon.language.List<ceylon.language.String>> iter = cList.iterator();
        ceylon.language.List<ceylon.language.String> list1 = (ceylon.language.List<ceylon.language.String>)iter.next();
        assertTrue(list1.contains(ceylon.language.String.instance("foo")));
        assertTrue(list1.contains(ceylon.language.String.instance("bar")));
        assertFalse(list1.contains(ceylon.language.String.instance("baz")));
        
        ceylon.language.List<ceylon.language.String> list2 = (ceylon.language.List<ceylon.language.String>)iter.next();
        assertFalse(list2.contains(ceylon.language.String.instance("foo")));
        assertTrue(list2.contains(ceylon.language.String.instance("bar")));
        assertTrue(list2.contains(ceylon.language.String.instance("baz")));
        
        assertTrue(iter.next() instanceof Finished);
        
        java.util.List<java.util.List<String>> rtList = wrapping.inverted().wrap(cList);
        
        assertTrue(list == rtList);
    }
}

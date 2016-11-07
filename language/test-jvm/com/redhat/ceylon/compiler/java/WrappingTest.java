package com.redhat.ceylon.compiler.java;

import static org.junit.Assert.*;
import org.junit.Test;

import com.redhat.ceylon.compiler.java.wrapping.Wrapping;
import com.redhat.ceylon.compiler.java.wrapping.Wrappings;

public class WrappingTest {

    @Test
    public void testToCeylonSetString() {
        java.util.Set<String> set = new java.util.HashSet<String>();
        set.add("foo");
        set.add("bar");
        set.add("baz");
        
        Wrapping<java.util.Set<String>, ceylon.language.Set<ceylon.language.String>> wrapping = Wrappings.toCeylonSet(ceylon.language.String.$TypeDescriptor$);
        ceylon.language.Set<ceylon.language.String> cSet = wrapping.wrap(set);
        
        assertTrue(cSet.contains(ceylon.language.String.instance("foo")));
        assertTrue(cSet.contains(ceylon.language.String.instance("bar")));
        assertTrue(cSet.contains(ceylon.language.String.instance("baz")));
        assertFalse(cSet.contains(ceylon.language.String.instance("gee")));
        
        java.util.Set<String> set2 = wrapping.inverted().wrap(cSet);
        
        assertTrue(set == set2);
    }
    
    @Test
    public void testToCeylonListString() {
        java.util.List<String> list = new java.util.ArrayList<String>();
        list.add("foo");
        list.add("bar");
        list.add("baz");
        
        Wrapping<java.util.List<String>, ceylon.language.List<ceylon.language.String>> wrapping = Wrappings.toCeylonList(ceylon.language.String.$TypeDescriptor$);
        ceylon.language.List<ceylon.language.String> cList = wrapping.wrap(list);
        
        assertTrue(cList.contains(ceylon.language.String.instance("foo")));
        assertTrue(cList.contains(ceylon.language.String.instance("bar")));
        assertTrue(cList.contains(ceylon.language.String.instance("baz")));
        assertFalse(cList.contains(ceylon.language.String.instance("gee")));
        
        java.util.List<String> list2 = wrapping.inverted().wrap(cList);
        
        assertTrue(list == list2);
    }
    
    @Test
    public void testToCeylonMapString() {
        java.util.Map<String,Long> map = new java.util.HashMap<String,Long>();
        map.put("foo", 1L);
        map.put("bar", 2L);
        map.put("baz", 3L);
        
        Wrapping<java.util.Map<String,Long>, ceylon.language.Map<ceylon.language.String,ceylon.language.Integer>> wrapping = Wrappings.toCeylonMap(
                ceylon.language.String.$TypeDescriptor$,
                ceylon.language.Integer.$TypeDescriptor$);
        ceylon.language.Map<ceylon.language.String,ceylon.language.Integer> cMap = wrapping.wrap(map);
        
        assertEquals(ceylon.language.Integer.instance(1), cMap.get(ceylon.language.String.instance("foo")));
        assertEquals(ceylon.language.Integer.instance(2), cMap.get(ceylon.language.String.instance("bar")));
        assertEquals(ceylon.language.Integer.instance(3), cMap.get(ceylon.language.String.instance("baz")));
        assertEquals(null, cMap.get(ceylon.language.String.instance("gee")));
        
        java.util.Map<String,Long> map2 = wrapping.inverted().wrap(cMap);
        
        assertTrue(map == map2);
    }
    
}

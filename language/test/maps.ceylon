// tests for Map interface

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared formal Entry<Key, Item>[] entries;
}

class MapTest<Key, Item>(<Key->Item>* entry)
            extends Object()
            satisfies MapTestBase<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared actual Entry<Key, Item>[] entries = entry.sequence();
    shared actual Integer size { return entries.size; }
    shared actual Boolean empty { return entries.empty; }
    shared actual MapTest<Key, Item> clone() { return this; }
    shared actual Iterator<Key->Item> iterator() { return entries.iterator(); }
    shared actual Item? get(Object key) {
        for (e in entries) {
            if (e.key == key) { return e.item; }
        }
        return null;
    }
    shared actual Boolean defines(Object key) {
        for (e in entries) {
            if (e.key == key) { return true; }
        }
        return false;
    }
}

@test
shared void testMaps() {
    value m1 = MapTest<Integer, String>(1->"A", 2->"B", 3->"C", 4->"B");
    check(m1.count((Entry<Integer,String> x) => x.key==2)==1, "Map.count(2->B) is `` m1.count((Entry<Integer,String> x) => x.key==2) `` instead of 1");
    check(m1.count((Entry<Integer,String> x) => x.key==100)==0, "Map.count 2");
    check(2->"B" in m1, "Map.contains(2->B) should be true");
    check(!(4.2 in m1), "Map.contains 2");
    check(!(1->"C" in m1), "Map.contains 3");
    check(m1.clone() == m1, "Map.clone/equals");
    check(m1 != 5, "Map.equals");
    check(m1.defines(4), "Map.defines(4) should be true");
    check(!m1.defines(5), "Map.defines 2");
    check(!m1.defines("hi"), "Map.defines 3");
    check(m1[4] exists, "Map.get(4) should exist");
    check(!m1[5] exists, "Map.item 2");
    check(!m1["hi"] exists, "Map.item 3");
    check(!m1.iterator().next() is Finished, "Map.iterator");
    check(m1.items.size==m1.size, "Map.items 1");
    for (e in m1) {
        check(e.item in m1.items, "Map.items 2");
    }
    check(m1.keys.size==m1.size, "Map.keys 1");
    for (e in m1) {
        check(e.key in m1.keys, "Map.keys.contains(`` e.key ``) should be true");
    }
    //check("B"->SetTest(2, 4) in m1.inverse, "Map.inverse should contain B->Set(2,4)");
    //check(m1.inverse.size==3, "Map.inverse 1: sizes `` m1.inverse.size `` should be 3");
    value m2 = m1.mapItems((Integer k, String v) => k*100);
    check(1->100 in m2, "Map.mapItems");
    for (k->v in m2) {
        if (v%100 != 0) { fail("Map.mapItems [2]"); }
    }
    //LazyMap
    check(LazyMap({}).size==0, "empty LazyMap()");
    value m = LazyMap({"a"->1, "b"->2, "c"->3});
    check(m.size == 3, "LazyMap size");
    if (exists v=m["a"]) {
        check(v==1, "LazyMap item");
    } else { fail("LazyMap item"); }
    check(m.fold(0)((Integer x, String->Integer e) => x+e.item)==6, "LazyMap fold");
    check(m.every((String->Integer e) => e.key in "abc"), "LazyMap every");
    check("b"->2 in m, "LazyMap contains");
    
    // emptyMap
    check(0 == emptyMap.size, "emptyMap.size");
    check(emptyMap.empty, "emptyMap.empty");
    check(!emptyMap.contains(1->1), "emptyMap.contains");
    check(!emptyMap.containsAny({1->1, 2->2}), "emptyMap.containsAny");
    check(!emptyMap.containsEvery({1->1, 2->2}), "emptyMap.containsEvery");
    check(0 == emptyMap.count{function selecting(Nothing->Nothing nowt) => true;}, "emptyMap.selecting");
    check(emptyMap.filter{function selecting(Nothing->Nothing nowt) => true;}.empty, "emptyMap.filter");
    
    
    value squares = map { for (i in 0..5) i->i^2 };
    check(squares==map{0->0, 1->1, 2->4, 3->9, 4->16, 5->25}, "map");
    check(squares.inverse()==map{0->[0], 1->[1], 4->[2], 9->[3], 16->[4], 25->[5]}, "map inverse 1");
    check(map{1->"hello", 2->"hello", 3->"goodbye"}.inverse()==map{"goodbye"->[3], "hello"->[1,2]},  "map inverse 2");
    value posZeroNeg = (-2..2).group(0.compare);
    check(posZeroNeg==map{smaller->[1, 2], larger->[-2, -1], equal->[0]}, "group");
    check(map { 1->"one", 0->null, 2->"two" }.coalescedMap==map { 1->"one", 2->"two" }, "coalescedMap");

    // performant Sequences from inverse(), #6278
    value bigMap = map((1..25k).map((i) => i -> 0));
    assert (exists allItems = bigMap.inverse()[0]);
    value start = system.nanoseconds;
    allItems.findLast(25k.equals); // slow
    check(system.nanoseconds - start < 1G, "reasonable performance from result of map.inverse()");
}

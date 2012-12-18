// tests for Map interface

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared formal Entry<Key, Item>[] entries;
}

class MapTest<Key, Item>(Key->Item... entry)
            extends Object()
            satisfies MapTestBase<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared actual Entry<Key, Item>[] entries = entry.sequence;
    shared actual Integer size { return entries.size; }
    shared actual Boolean empty { return entries.empty; }
    shared actual MapTest<Key, Item> clone { return this; }
    shared actual Iterator<Key->Item> iterator { return entries.iterator; }
    shared actual Item? item(Object key) {
        for (e in entries) {
            if (e.key == key) { return e.item; }
        }
        return null;
    }
}

void testMaps() {
    value m1 = MapTest<Integer, String>(1->"A", 2->"B", 3->"C", 4->"B");
    check(m1.count((Entry<Integer,String> x) x.key==2)==1, "Map.count(2->B) is " m1.count((Entry<Integer,String> x) x.key==2) " instead of 1");
    check(m1.count((Entry<Integer,String> x) x.key==100)==0, "Map.count 2");
    check(2->"B" in m1, "Map.contains(2->B) should be true");
    check(!(4.2 in m1), "Map.contains 2");
    check(!(1->"C" in m1), "Map.contains 3");
    check(m1.clone == m1, "Map.clone/equals");
    check(m1 != 5, "Map.equals");
    check(m1.defines(4), "Map.defines(4) should be true");
    check(!m1.defines(5), "Map.defines 2");
    check(!m1.defines("hi"), "Map.defines 3");
    check(m1[4] exists, "Map.item(4) should exist");
    check(!m1[5] exists, "Map.item 2");
    check(!m1["hi"] exists, "Map.item 3");
    check(!m1.iterator.next() is Finished, "Map.iterator");
    check(m1.values.size==m1.size, "Map.values 1");
    for (e in m1) {
        check(e.item in m1.values, "Map.values 2");
    }
    check(m1.keys.size==m1.size, "Map.keys 1");
    for (e in m1) {
        check(e.key in m1.keys, "Map.keys.contains(" e.key ") should be true");
    }
    check("B"->SetTest(2, 4) in m1.inverse, "Map.inverse should contain B->Set(2,4)");
    check(m1.inverse.size==m1.size, "Map.inverse 1");
    value m2 = m1.mapItems((Integer k, String v) k*100);
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
    check(m.fold(0, (Integer x, String->Integer e) x+e.item)==6, "LazyMap fold");
    check(m.every((String->Integer e) e.key in "abc"), "LazyMap every");
    check("b"->2 in m, "LazyMap contains");
}

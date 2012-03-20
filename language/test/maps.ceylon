// tests for Map and Set interface
// Not much functionality to test here, as concrete interface members are not
// supported yet. But we can test if all members can be implemented, everything
// compiles, types are correct, etc.

interface SetTestBase<out Element> satisfies Set<Element>
            given Element satisfies Object {
    shared formal Element[] elements;
}

class SetTest<Element>(Element... elements)
            satisfies SetTestBase<Element>
            given Element satisfies Object {
    shared actual Element[] elements = elements;
    shared actual Integer size { return elements.size; }
    shared actual Boolean empty { return elements.empty; }
    shared actual SetTest<Element> clone { return this; }
    shared actual Iterator<Element> iterator { return elements.iterator; }
    shared actual Integer hash { return elements.hash; }
    shared actual Boolean equals(Object other) {
        if (is SetTestBase<Object> other) {
            return other.elements == elements;
        }
        return false;
    }
    shared actual Boolean contains(Object element) {
        for (e in elements) {
            if (e == element) { return true; }
        }
        return false;
    }
    shared actual Boolean containsAny(Object... elems) {
        for (e in elems) {
            if (contains(e)) { return true; }
        }
        return false;
    }
    shared actual Boolean containsEvery(Object... elems) {
        for (e in elems) {
            if (!contains(e)) { return false; }
        }
        return true;
    }
    shared actual Integer count(Object element) {
        return contains(element) then 1 else 0;
    }
    shared actual Boolean superset(Set<Object> set) {
        for (e in set) {
            if (!contains(e)) { return false; }
        }
        return true;
    }
    shared actual Boolean subset(Set<Object> set) {
        for (e in elements) {
            if (!(e in set)) { return false; }
        }
        return true;
    }
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = SequenceBuilder<Element|Other>();
        sb.appendAll(elements...);
        for (e in set) {
            for (e2 in elements) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(sb.sequence...);
    }
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
                given Other satisfies Object {
        //value sb = SequenceBuilder<Element&Other>();
        //for (e in set) {
        //    if (e is Element) {
        //        for (e2 in elements) {
        //            if (e2 == e) {
        //                sb.append(e);
        //                break;
        //            }
        //        }
        //    }
        //}
        //return SetTest(sb.sequence...);
        return bottom;
    }
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = SequenceBuilder<Element|Other>();
        for (e in elements) {
            for (e2 in set) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        for (e in set) {
            for (e2 in elements) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(sb.sequence...);
    }
    shared actual Set<Element> complement<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = SequenceBuilder<Element>();
        for (e in elements) {
            for (e2 in set) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(sb.sequence...);
    }
}

interface MapTestBase<out Key, out Item> satisfies Map<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared formal Entry<Key, Item>[] entries;
}

class MapTest<Key, Item>(Key->Item... entries)
            satisfies MapTestBase<Key, Item>
            given Key satisfies Object
            given Item satisfies Object {
    shared actual Entry<Key, Item>[] entries = entries;
    shared actual Boolean equals(Object other) {
        if (is MapTestBase<Object, Object> other) {
            return other.entries == entries;
        }
        return false;
    }
    shared actual Integer hash { return entries.hash; }
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
    shared actual Item?[] items(Object... keys) {
        value sb = SequenceBuilder<Item?>();
        for (k in keys) { sb.append(item(k)); }
        return sb.sequence;
    }
    shared actual Boolean defines(Object key) {
        for (e in entries) {
            if (e.key == key) { return true; }
        }
        return false;
    }
    shared actual Boolean definesAny(Object... keys) {
        for (k in keys) {
            if (defines(k)) { return true; }
        }
        return false;
    }
    shared actual Boolean definesEvery(Object... keys) {
        for (k in keys) {
            if (!defines(k)) { return false; }
        }
        return true;
    }
    shared actual Boolean contains(Object element) {
        if (is Object->Object element) {
            if (exists it = item(element.key)) { return it == element.item; }
        }
        return false;
    }
    shared actual Boolean containsAny(Object... element) {
        for (e in element) {
            if (contains(e)) { return true; }
        }
        return false;
    }
    shared actual Boolean containsEvery(Object... element) {
        for (e in element) {
            if (!contains(e)) { return false; }
        }
        return true;
    }
    shared actual Integer count(Object element) {
        return contains(element) then 1 else 0;
    }
    shared actual Set<Key> keys {
        value sb = SequenceBuilder<Key>();
        for (e in entries) { sb.append(e.key); }
        return SetTest(sb.sequence...);
    }
    shared actual Collection<Item> values {
        value sb = SequenceBuilder<Item>();
        for (e in entries) { sb.append(e.item); }
        return array(sb.sequence...);
    }
    shared actual Map<Item, Set<Key>> inverse {
        value sb = SequenceBuilder<Item->Set<Key>>();
        variable Integer count := 0;
        for (e in entries) {
            value keySB = SequenceBuilder<Key>();
            variable Integer cnt2 := 0; 
            variable Boolean duplicate := false;
            for (e2 in entries) {
                if (e2.item == e.item) {
                    if (cnt2 < count) {
                        duplicate := true;
                        break;
                    }
                    keySB.append(e2.key);
                }
                ++cnt2;
            }
            if (!duplicate) {
                sb.append(e.item->SetTest(keySB.sequence...));
            }
            ++count;
        }
        return MapTest(sb.sequence...);
    }
}

void testMaps() {
    value m1 = MapTest<Integer, String>(1->"A", 2->"B", 3->"C", 4->"B");
    assert(m1.count(2->"B")==1, "Map.count(2->B) is " m1.count(2->"B") " instead of 1");
    assert(m1.count(4.2)==0, "Map.count 2");
    assert(2->"B" in m1, "Map.contains(2->B) should be true");
    assert(!(4.2 in m1), "Map.contains 2");
    assert(!(1->"C" in m1), "Map.contains 3");
    assert(m1.clone == m1, "Map.clone/equals");
    assert(m1 != 5, "Map.equals");
    assert(m1.defines(4), "Map.defines(4) should be true");
    assert(!m1.defines(5), "Map.defines 2");
    assert(!m1.defines("hi"), "Map.defines 3");
    assert(exists m1[4], "Map.item(4) should exist");
    assert(!exists m1[5], "Map.item 2");
    assert(!exists m1["hi"], "Map.item 3");
    assert(!(is Finished m1.iterator.next()), "Map.iterator");
    assert(m1.values.size==m1.size, "Map.values 1");
    for (e in m1) {
        assert(e.item in m1.values, "Map.values 2");
    }
    assert(m1.keys.size==m1.size, "Map.keys 1");
    for (e in m1) {
        assert(e.key in m1.keys, "Map.keys.contains(" e.key ") should be true");
    }
    assert("B"->SetTest(2, 4) in m1.inverse, "Map.inverse should contain B->Set(2,4)");
}

void testSets() {
    value s1 = SetTest<Integer>(1, 2, 3);
    value emptySet = SetTest<Bottom>();
    assert(s1.count(2)==1, "Set.count 1");
    assert(s1.count(4.2)==0, "Set.count 2");
    assert(2 in s1, "Set.contains 1");
    assert(!(4.2 in s1), "Set.contains 2");
    assert(!(4 in s1), "Set.contains 3");
    assert(s1.clone == s1, "Set.clone/equals");
    assert(s1 != 5, "Set.equals");
    assert(!(is Finished s1.iterator.next()), "Set.iterator");
    assert(emptySet.subset(s1), "Set.subset 1");
    assert(!s1.subset(emptySet), "Set.subset 2");
    assert(!emptySet.superset(s1), "Set.superset 1");
    assert(s1.superset(emptySet), "Set.superset 2");
    assert(s1.union(emptySet)==s1, "Set.union 1");
    assert(emptySet.union(s1)==s1, "Set.union 2");
    assert(s1.complement(emptySet)==s1, "Set.complement 1");
    assert(emptySet.complement(s1)==emptySet, "Set.complement 2");
}

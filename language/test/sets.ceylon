// tests for Set interface
// Not much functionality to test here, as concrete interface members are not
// supported yet. But we can test if all members can be implemented, everything
// compiles, types are correct, etc.

interface SetTestBase<out Element> satisfies Set<Element>
            given Element satisfies Object {
    shared formal Element[] elems;
}

class ArrayBuilder<Element>() {
    
    "The storage"
    variable Array<Element>? storage = null;
    "The number of items in [[array]] which have actually been appended."
    variable Integer length = 0;
    
    "Resize policy"
    Integer newSize(
        Integer existingSize, Integer extra) {
        value requiredSize = length+extra;
        variable value result = existingSize*2+2;
        "Overflow"
        assert (requiredSize > 0);
        if (result < requiredSize) {
            result = requiredSize;
        }
        "Required array too large"
        assert (result <= runtime.maxArraySize);
        return result;
    }
    
    "Returns the storage array ready for storing [[extra]] more elements.
     Reallocates and copies existing entries if needed."
    Array<Element> getStorage(Integer extra, Element example) {
        // extra should be > 0
        if (exists s=storage) {
            if (s.size >= length + extra) {
                return s;
            } else {
                value newStorage = arrayOfSize<Element>(newSize(s.size, extra), example);
                s.copyTo(newStorage);
                storage = newStorage;
                return newStorage;
            }
        } else {
            value newStorage = arrayOfSize<Element>(newSize(0, extra), example);
            storage = newStorage;
            return newStorage;
        }
    }
    
    shared ArrayBuilder<Element> append(Element element) {
        value store = getStorage(1, element);
        store.set(length, element);
        length += 1;
        return this;
    }
    
    shared ArrayBuilder<Element> appendAll({Element*} elements) {
        // TODO If we know the size of elements efficiently we can just do one allocation.
        for (element in elements) {
            append(element);
        }
        return this;
    }
    shared actual String string {
        return sequence().string;
    }
    
    shared Element[] sequence() {
        if (exists s=storage) {
            return s.take(length).sequence();
        }
        return empty;
    }
    
    //shared Integer size => length;

}


class SetTest<Element>(Element* element) extends Object()
            satisfies SetTestBase<Element>
            given Element satisfies Object {
    shared actual Element[] elems = element.sequence();
    shared actual Integer size { return elems.size; }
    shared actual Boolean empty { return elems.empty; }
    shared actual SetTest<Element> clone() { return this; }
    shared actual Iterator<Element> iterator() { return elems.iterator(); }
    shared actual Set<Element|Other> union<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = ArrayBuilder<Element|Other>();
        sb.appendAll(elems);
        for (e in set) {
            for (e2 in elems) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(*sb.sequence());
    }
    shared actual Set<Element&Other> intersection<Other>(Set<Other> set)
                given Other satisfies Object {
        return nothing;
    }
    shared actual Set<Element|Other> exclusiveUnion<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = ArrayBuilder<Element|Other>();
        for (e in elems) {
            for (e2 in set) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        for (e in set) {
            for (e2 in elems) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(*sb.sequence());
    }
    shared actual Set<Element> complement<Other>(Set<Other> set)
                given Other satisfies Object {
        value sb = ArrayBuilder<Element>();
        for (e in elems) {
            for (e2 in set) {
                if (e2 == e) { break; }
            } else {
                sb.append(e);
            }
        }
        return SetTest(*sb.sequence());
    }
}

@test
shared void testSets() {
    value s1 = SetTest<Integer>(1, 2, 3);
    value es = SetTest<Nothing>();
    check(s1.count((Integer x) => x==2)==1, "Set.count 1");
    check(s1.count((Integer x) => x==100)==0, "Set.count 2");
    check(2 in s1, "Set.contains 1");
    check(!(4.2 in s1), "Set.contains 2");
    check(!(4 in s1), "Set.contains 3");
    check(s1.clone() == s1, "Set.clone/equals");
    check(s1 != 5, "Set.equals");
    check(!s1.iterator().next() is Finished, "Set.iterator");
    check(es.subset(s1), "Set.subset 1");
    check(!s1.subset(es), "Set.subset 2");
    check(!es.superset(s1), "Set.superset 1");
    check(s1.superset(es), "Set.superset 2");
    check(s1.union(es)==s1, "Set.union 1 ``s1.union(es)``");
    check(es.union(s1)==s1, "Set.union 2 ``es.union(s1)``");
    check(s1.complement(es)==s1, "Set.complement 1");
    check(es.complement(s1)==es, "Set.complement 2");
    //LazySet
    check(LazySet({}).size==0, "empty LazySet()");
    value s2 = LazySet({3,4,5,"a"});
    value s3 = LazySet({"a", "b", "c", 5});
    value s4 = SetTest(5);
    check(s2.size==s3.size, "LazySet.size");
    check(4 in s2, "LazySet.contains");
    check(s2|s1 == LazySet({1,2,3,4,5,"a"}), "LazySet.union");
    check(s2&s3 == LazySet({"a", 5}), "LazySet.intersection");
    //check(s2^s3 == LazySet({3,4,"b","c"}), "LazySet.exclusiveUnion");
    check(s2~s3 == LazySet({3, 4}), "LazySet.complement 1");
    check(s3~s2 == LazySet({"b", "c"}), "LazySet.complement 2");
    check(s2.superset(s4), "LazySet.superset 1");
    check(s3.superset(s4), "LazySet.superset 2");
    check(s2.subset(LazySet({"a","b","c",3,4,5,"d","e",1,2,3})), "LazySet.subset");
    
    // emptySet
    check(0 == emptySet.size, "emptySet.size");
    check(emptySet.empty, "emptySet.empty");
    check(!emptySet.contains(1), "emptySet.contains");
    check(!emptySet.containsAny({1, 2}), "emptySet.containsAny");
    check(!emptySet.containsEvery({1, 2}), "emptySet.containsEvery");
    check(!emptySet.superset(s2), "emptySet.superset");
    check(emptySet.superset(emptySet), "emptySet.superset 2");
    check(emptySet.subset(s2), "emptySet.subset");
    check(emptySet.union(s2) == s2, "emptySet.union");
    check(emptySet.exclusiveUnion(s2) == s2, "emptySet.exclusiveUnion");
    check(emptySet.intersection(s2) == emptySet, "emptySet.intersection");
    check(emptySet.complement(s2) == emptySet, "emptySet.complement");
    check(0 == emptySet.count{function selecting(Nothing nowt) => true;}, "emptySet.selecting");
    check(emptySet.filter{function selecting(Nothing nowt) => true;}.empty, "emptySet.filter");
    
    check(set {"hello", "world", "hello"}==set{"hello", "world"}, "set");
}

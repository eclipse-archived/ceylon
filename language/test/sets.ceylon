// tests for Set interface
// Not much functionality to test here, as concrete interface members are not
// supported yet. But we can test if all members can be implemented, everything
// compiles, types are correct, etc.

interface SetTestBase<out Element> satisfies Set<Element>
            given Element satisfies Object {
    shared formal Element[] elements;
}

class SetTest<Element>(Element... element) extends Object()
            satisfies SetTestBase<Element>
            given Element satisfies Object {
    shared actual Element[] elements = element.sequence;
    shared actual Integer size { return elements.size; }
    shared actual Boolean empty { return elements.empty; }
    shared actual SetTest<Element> clone { return this; }
    shared actual Iterator<Element> iterator { return elements.iterator; }
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

void testSets() {
    value s1 = SetTest<Integer>(1, 2, 3);
    value emptySet = SetTest<Nothing>();
    check(s1.count((Integer x) x==2)==1, "Set.count 1");
    check(s1.count((Integer x) x==100)==0, "Set.count 2");
    check(2 in s1, "Set.contains 1");
    check(!(4.2 in s1), "Set.contains 2");
    check(!(4 in s1), "Set.contains 3");
    check(s1.clone == s1, "Set.clone/equals");
    check(s1 != 5, "Set.equals");
    check(!s1.iterator.next() is Finished, "Set.iterator");
    check(emptySet.subset(s1), "Set.subset 1");
    check(!s1.subset(emptySet), "Set.subset 2");
    check(!emptySet.superset(s1), "Set.superset 1");
    check(s1.superset(emptySet), "Set.superset 2");
    check(s1.union(emptySet)==s1, "Set.union 1");
    check(emptySet.union(s1)==s1, "Set.union 2");
    check(s1.complement(emptySet)==s1, "Set.complement 1");
    check(emptySet.complement(s1)==emptySet, "Set.complement 2");
    //LazySet
    check(LazySet({}).size==0, "empty LazySet()");
    value s2 = LazySet({3,4,5,"a"});
    value s3 = LazySet({"a", "b", "c", 5});
    value s4 = SetTest(5);
    check(s2.size==s3.size, "LazySet.size");
    check(4 in s2, "LazySet.contains");
    check(s2|s1 == LazySet({1,2,3,4,5,"a"}), "LazySet.union");
    check(s2&s3 == LazySet({"a", 5}), "LazySet.intersection");
    check(s2^s3 == LazySet({3,4,"b","c"}), "LazySet.exclusiveUnion");
    check(s2~s3 == LazySet({3, 4}), "LazySet.complement 1");
    check(s3~s2 == LazySet({"b", "c"}), "LazySet.complement 2");
    check(s2.superset(s4), "LazySet.superset 1");
    check(s3.superset(s4), "LazySet.superset 2");
    check(s2.subset(LazySet({"a","b","c",3,4,5,"d","e",1,2,3})), "LazySet.subset");
}

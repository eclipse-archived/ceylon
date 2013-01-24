//Simple implementation of single interfaces to verify correct compilation and behavior
class MyCategory() satisfies Category {
    shared actual Boolean contains(Object e) {
        if (is Integer e) {
            return e>0 && e<11;
        }
        return false;
    }
}
class MyCollection() satisfies Collection<Character> {
    value s = "hello";
    shared actual Integer size { return s.size; }
    shared actual Iterator<Character> iterator { return s.iterator; }
    shared actual MyCollection clone { return MyCollection(); }
}
class MyComparable() satisfies Comparable<MyComparable> {
    value p = process.milliseconds;
    shared actual Comparison compare(MyComparable other) {
        return p <=> other.p;
    }
}
class MyContainerWithLastElement() satisfies Container<Integer,Null> {
    shared actual Integer? first { return 1; }
    shared actual Integer? last { return 2; }
    shared actual Boolean empty { return false; }
    shared actual Boolean contains(Object element) => element == 1 || element == 2;
}
class MyContainerWithoutFirstElement() satisfies Container<Integer,Null> {
    shared actual Integer? first { return null; }
    shared actual Integer? last { return 2; }
    shared actual Boolean empty { return false; }
    shared actual Boolean contains(Object element) => element == 2;
}
class MyContainerWithoutLastElement() satisfies Container<Integer,Null> {
    shared actual Integer? first { return 1; }
    shared actual Integer? last { return null; }
    shared actual Boolean empty { return false; }
    shared actual Boolean contains(Object element) => element == 1;
}
class MyCloseable() satisfies Closeable {
    shared variable Boolean opened = false;
    shared actual void open() { opened=true;}
    shared actual void close(Exception? e) {opened=false;}
}
class MyContainer() satisfies EmptyContainer {
    shared actual Null first { return null; }
    shared actual Null last { return null; }
    shared actual Boolean empty = true;
    shared actual Boolean contains(Object element) => false;
}
class MyIterator() satisfies Iterator<Integer> {
    variable value done = false;
    shared actual Integer|Finished next() {
        value r = done then finished else 1;
        done = true;
        return r;
    }
}
/*class MySequence() satisfies Sequence<Integer> {
    shared actual Integer lastIndex = 0;
    shared actual Integer first = 1;
    shared actual Integer[] rest = {};
    shared actual Integer hash = 1;
    shared actual Boolean equals(Object other) { return false; }
    shared actual MySequence clone { return MySequence(); }
    shared actual Sequence<Integer> reversed { return this; }
    shared actual Integer? get(Integer index) {
        return index==0 then 1 else null;
    }
    shared actual Integer[] segment(Integer from, Integer length) {
        return from==0 && length>0 then this else {};
    }
    shared actual Integer[] span(Integer from, Integer? to) {
        return from==0 then this else {};
    }
}*/
class MyRanged() satisfies Ranged<Integer, Iterable<Character>> {
    value contents = `a`..`z`;
    shared actual Iterable<Character> span(Integer from, Integer to) {
        return contents.span(from, to);
    }
    shared actual Iterable<Character> spanFrom(Integer from) {
        return contents.spanFrom(from);
    }
    shared actual Iterable<Character> spanTo(Integer to) {
        return contents.spanTo(to);
    }
    shared actual Iterable<Character> segment(Integer from, Integer length) {
        return contents.segment(from, length);
    }
}
class MyOrdinal(prev, next) satisfies Ordinal<MyOrdinal> {
    shared variable MyOrdinal? prev;
    shared variable MyOrdinal? next;
    shared actual MyOrdinal successor { return next else this; }
    shared actual MyOrdinal predecessor { return prev else this; }
}
class MyNumeric(Integer x) satisfies Numeric<MyNumeric> & Exponentiable<MyNumeric,Integer> {
    shared actual MyNumeric minus(MyNumeric other) { return MyNumeric(x-other.x); }
    shared actual MyNumeric plus(MyNumeric other) { return MyNumeric(x+other.x); }
    shared actual MyNumeric times(MyNumeric other) { return MyNumeric(x*other.x); }
    shared actual MyNumeric divided(MyNumeric other) { return MyNumeric(x/other.x); }
    shared actual MyNumeric positiveValue { return MyNumeric(+x); }
    shared actual MyNumeric negativeValue { return MyNumeric(-x); }
    shared actual MyNumeric power(Integer exp) { return MyNumeric(x**exp); }
    shared actual Boolean equals(Object o) {
        if (is MyNumeric o) {
            return o.x == x;
        }
        return false;
    }
}
class MyInvertable(Integer x) satisfies Invertable<MyInvertable> {
    shared actual MyInvertable negativeValue { return MyInvertable(-x); }
    shared actual MyInvertable positiveValue { return MyInvertable(+x); }
    shared actual Boolean equals(Object o) {
        if (is MyInvertable o) {
            return o.x == x;
        }
        return false;
    }
}
class MyCorrespondence() satisfies Correspondence<Integer, Character> {
    value a = "abcdef";
    shared actual Character? get(Integer k) { return a[k]; }
}

void testSatisfaction() {
    value category = MyCategory();
    check(5 in category, "Category.contains [1]");
    check(!20 in category, "Category.contains [2]");
    check(category.containsEvery {1,2,3,4,5}, "Category.containsEvery [1]");
    check(!category.containsEvery {0,1,2,3}, "Category.containsEvery [2]");
    check(category.containsAny {0,1,2,3}, "Category.containsAny [1]");
    check(!category.containsAny {0,11,12,13}, "Category.containsAny [2]");
    value collection = MyCollection();
    check(!collection.empty, "Collection.empty");
    check(`l` in collection, "Collection.contains");
    check(collection.string == "{ h, e, l, l, o }", "Collection.string");
    check(MyComparable() <= MyComparable(), "Comparable.compare");
    variable Container<Integer,Null> cwfe = MyContainerWithLastElement();
    check(cwfe.first exists, "Container.first [1]");
    check(cwfe.last exists,  "Container.last  [1]");
    cwfe = MyContainerWithoutFirstElement();
    check(!cwfe.first exists, "Container.first [2]");
    check(cwfe.last exists, "Container.last [2]");
    cwfe = MyContainerWithoutLastElement();
    check(cwfe.first exists, "Container.first [1]");
    check(!cwfe.last exists, "Container.first [2]");
    value clsbl = MyCloseable();
    check(!clsbl.opened, "Closeable [1]");
    clsbl.open();
    check(clsbl.opened, "Closeable [2]");
    clsbl.close(null);
    check(!clsbl.opened, "Closeable [3]");
    check(MyContainer().empty, "Container");
    variable Sequential<Integer> myfixed = {};//MyNone();
    check(!myfixed nonempty, "None");
    myfixed = [1];//MySome();
    check(myfixed nonempty, "Some");
    value myiter = MyIterator();
    if (is Integer ii=myiter.next()) {
        check(ii==1, "Iterator [1]");
    } else { fail("Iterator [1]"); }
    if (!myiter.next() is Finished) {
        fail("Iterator [2]");
    }
    /*check(MySequence().last==MySequence().first, "Sequence[1]");
    check(!nonempty MySequence().rest, "Sequence[2]");
    check(MySequence().reversed==MySequence(), "Sequence[3]");*/
    check(MyRanged().spanFrom(23).sequence=={`x`, `y`, `z`}, "Ranged[1]");
    check(MyRanged().spanTo(2).sequence=={`a`,`b`,`c`}, "Ranged[2]");
    check(MyRanged().span(1,3).sequence=={`b`,`c`,`d`}, "Ranged[3]");
    check(MyRanged().segment(3,1).sequence=={`d`}, "Ranged[4]");

    variable value ord1 = MyOrdinal(null,null);
    variable value ord2 = MyOrdinal(ord1, null);
    variable value ord3 = MyOrdinal(ord2, ord1);
    ord1.next = ord2;
    ord2.next = ord3;
    check(++ord1==ord2, "Ordinal [1]");
    check(--ord3==ord2, "Ordinal [2]");
    ord3++;
    check(++ord3==--ord1, "Ordinal [3]");

    check(MyNumeric(1)+MyNumeric(1)==MyNumeric(2), "Numeric[1]");
    check(MyNumeric(2)-MyNumeric(1)==MyNumeric(1), "Numeric[2]");
    check(MyNumeric(2)*MyNumeric(2)==MyNumeric(4), "Numeric[3]");
    check(MyNumeric(6)/MyNumeric(3)==MyNumeric(2), "Numeric[4]");
    check(MyNumeric(1)*MyNumeric(-1)==-MyNumeric(1), "Numeric[5]");
    check(MyNumeric(2)**3==MyNumeric(8), "Numeric[6]");

    check(MyInvertable(-1)==-MyInvertable(1), "Invertable");

    value corr = MyCorrespondence();
    check(corr[0] exists, "Correspondence[1]");
    check(!corr[100] exists, "Correspondence[2]");
    check(corr.defines(3), "Correspondence[3]");
    check(corr[4] is Character, "Correspondence[4]");
}

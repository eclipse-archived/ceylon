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
class MyContainerWithLastElement() satisfies ContainerWithFirstElement<Integer,Nothing> {
    shared actual Integer? first { return 1; }
    shared actual Integer? last { return 2; }
    shared actual Boolean empty { return false; }
}
class MyContainerWithoutFirstElement() satisfies ContainerWithFirstElement<Integer,Nothing> {
    shared actual Integer? first { return null; }
    shared actual Integer? last { return 2; }
    shared actual Boolean empty { return false; }
}
class MyContainerWithoutLastElement() satisfies ContainerWithFirstElement<Integer,Nothing> {
    shared actual Integer? first { return 1; }
    shared actual Integer? last { return null; }
    shared actual Boolean empty { return false; }
}
class MyCloseable() satisfies Closeable {
    shared variable Boolean opened := false;
    shared actual void open() { opened:=true;}
    shared actual void close(Exception? e) {opened:=false;}
}
class MyContainer() satisfies Container {
    shared actual Boolean empty = true;
}
class MySized(Integer s) satisfies Sized {
    shared actual Integer size { return s; }
}
/*class MyNone() satisfies None<Integer> {
    shared actual Nothing last { return null; }
    shared actual MyNone clone { return this; }
}*/
class MySome() satisfies Some<Integer> {
    shared actual Integer last = 1;
    shared actual Integer size = 2;
    shared actual FixedSized<Integer> rest { return {1}; }
    shared actual MySome clone { return this; }
    shared actual Iterator<Integer> iterator { return {2,1}.iterator; }
}
class MyIterator() satisfies Iterator<Integer> {
    variable value done := false;
    shared actual Integer|Finished next() {
        value r = done then exhausted else 1;
        done := true;
        return r;
    }
}

void testSatisfaction() {
    value category = MyCategory();
    assert(5 in category, "Category.contains [1]");
    assert(!20 in category, "Category.contains [2]");
    assert(category.containsEvery(1,2,3,4,5), "Category.containsEvery [1]");
    assert(!category.containsEvery(0,1,2,3), "Category.containsEvery [2]");
    assert(category.containsAny(0,1,2,3), "Category.containsAny [1]");
    assert(!category.containsAny(0,11,12,13), "Category.containsAny [2]");
    value collection = MyCollection();
    assert(!collection.empty, "Collection.empty");
    assert(`l` in collection, "Collection.contains");
    assert(collection.string == "{ h, e, l, l, o }", "Collection.string");
    assert(MyComparable() <= MyComparable(), "Comparable.compare");
    variable ContainerWithFirstElement<Integer,Nothing> cwfe := MyContainerWithLastElement();
    assert(exists cwfe.first, "ContainerWithFirstElement.first [1]");
    assert(exists cwfe.last,  "ContainerWithFirstElement.last  [1]");
    cwfe := MyContainerWithoutFirstElement();
    assert(!exists cwfe.first, "ContainerWithFirstElement.first [2]");
    assert(exists cwfe.last, "ContainerWithFirstElement.last [2]");
    cwfe := MyContainerWithoutLastElement();
    assert(exists cwfe.first, "ContainerWithFirstElement.first [1]");
    assert(!exists cwfe.last, "ContainerWithFirstElement.first [2]");
    value clsbl = MyCloseable();
    assert(!clsbl.opened, "Closeable [1]");
    clsbl.open();
    assert(clsbl.opened, "Closeable [2]");
    clsbl.close(null);
    assert(!clsbl.opened, "Closeable [3]");
    assert(MyContainer().empty, "Container");
    assert(MySized(0).empty, "Sized [1]");
    assert(!MySized(1).empty, "Sized [2]");
    variable FixedSized<Integer> myfixed := empty;//MyNone();
    assert(!nonempty myfixed, "None");
    myfixed := MySome();
    assert(nonempty myfixed, "Some");
    value myiter = MyIterator();
    if (is Integer ii=myiter.next()) {
        assert(ii==1, "Iterator [1]");
    } else { fail("Iterator [1]"); }
    if (!is Finished myiter.next()) {
        fail("Iterator [2]");
    }
}

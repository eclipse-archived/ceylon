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
}

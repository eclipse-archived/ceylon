class Bug1651Hollow<Element>() satisfies {Element+} {
    iterator() => {}.iterator();
}

void bug1651func<Element>(Element+ elements) {
    print(elements.first);
}

shared void bug1651() {
    try {
        bug1651func<String>(*Bug1651Hollow<String>());
    } catch (AssertionError e) {
        assert("Assertion failed: Sequence expected" == e.message);
    }
}
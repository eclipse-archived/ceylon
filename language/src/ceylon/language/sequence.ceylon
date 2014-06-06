"A [[Sequence]] of the given elements, or `Absent` if the iterable is empty.
 A [[Sequential]] can be obtained using the `else` operator:
 
     sequence(elements) else []
 "
by("Gavin")
shared [Element+]|Absent sequence<Element,Absent>(Iterable<Element, Absent> elements) 
        given Absent satisfies Null {
    if (is [Element+] elements) {
        return elements;
    }
    value array = Array(elements);
    if (array.empty) {
        assert (is Absent null);
        return null;
    }
    return ArraySequence(array);
}

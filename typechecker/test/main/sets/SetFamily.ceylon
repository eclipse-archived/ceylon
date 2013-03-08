abstract class SetFamily<Element,in S>() 
        given S satisfies Set {
    
    shared interface Set of S
            satisfies Iterable<Element> {
        shared formal void add(Element elem);
        shared formal Boolean contains1(Element elem);
        shared formal Boolean equals1(S that);
    } 

}

@error //pity this doesn't work!
class TreeSetFamily<Element>() 
        extends SetFamily<Element,TreeSet>() {
    shared class TreeSet()
            satisfies Set {
        actual shared void add(Element elem) {}
        actual shared Boolean contains1(Element elem) { 
            return nothing; 
        }
        actual shared Boolean equals1(TreeSet that) {
            return nothing;
        }
        shared actual Iterator<Element> iterator() => nothing;
    }
}
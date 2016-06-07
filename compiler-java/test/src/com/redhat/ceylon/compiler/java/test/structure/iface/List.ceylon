@noanno
shared interface Iterable<out Element=Anything, 
                            out Absent=Null>
        satisfies Category<>
        given Absent satisfies Null {
    
}
@noanno
shared interface Collection<out Element=Anything>
        satisfies Iterable<Element> {
}
@noanno
shared interface Ranged<in Index, out Element, out Subrange> 
        of Subrange
        satisfies Iterable<Element>
        given Subrange satisfies Ranged<Index,Element,Subrange> {
}
@noanno
shared interface List<out Element=Anything>
        satisfies Collection<Element> &
        Ranged<Integer,Element,List<Element>> {
    shared Anything m() {
        object o {}
        return o;
    }
}

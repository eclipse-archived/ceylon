shared interface Sequential<out Element>
        of Empty|Sequence<Element>
        satisfies List<Element> &
                  Ranged<Integer,Element[]> &
                  Cloneable<Element[]> {
    
    shared formal Element[] reversed;
    
}
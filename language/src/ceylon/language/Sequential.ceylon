shared interface Sequential<out Element>
        of Empty|Sequence<Element>
        satisfies List<Element> & FixedSized<Element> &
                  Ranged<Integer,Element[]> &
                  Cloneable<Sequential<Element>> {
    
    shared actual formal Element[] reversed;
    
}
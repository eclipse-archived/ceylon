public extension class CollectionsOfComparable<out X>(Collection<X> collection) 
        given X satisfies Comparable<X> {
        
    doc "The elements of the collection, sorted in natural order."
    public List<X> sortedElements() {
        return collection.sortedElements() by (X x, X y) (x<=>y);
    }
    
}
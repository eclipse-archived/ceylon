public extension class OpenListsOfComparable<out X>(OpenList<X> list) 
        given X satisfies Comparable<X> {
        
    doc "Reorder the elements of the list, according to the 
         natural order."
    public void resort() {
        list.resort() by (X x, X y) (x<=>y);
    }
    
}
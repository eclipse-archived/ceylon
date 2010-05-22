public extension class OpenListsOfComparable<out X>(OpenList<X> list) 
        where X>=Comparable<X> {
        
    doc "Reorder the elements of the list, according to the 
         natural order."
    public void resort() {
        list.resort() by (X x, X y) (x<=>y);
    }
    
}
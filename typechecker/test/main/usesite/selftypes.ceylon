void selfTypeCoverage() {
    
    interface Comparable<I> of I 
            given I satisfies Comparable<in I> {}
    
    class Int() satisfies Comparable<Int> {}
    
    Comparable<Int> k = Int();
    Int kk = k of Int;
    Comparable<out Int> i = Int();
    Int ii = i of Int;
    Comparable<in Int> j = Int();
    $error Int jj = j of Int;
}
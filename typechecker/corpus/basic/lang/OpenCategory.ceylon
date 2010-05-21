public mutable interface OpenCategory<in X> 
        satisfies Category {
    
    doc "Add the given objects to the category. Return the number of 
         objects which did not already belong to the category."
    public Natural add(X... objects);

}
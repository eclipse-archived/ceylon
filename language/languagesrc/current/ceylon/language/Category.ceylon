shared interface Category {
    
    doc "Determine if the given objects belong to the category.
         Return |true| iff all the given objects belong to the 
         category."
    shared formal Boolean contains(Object... objects);

}
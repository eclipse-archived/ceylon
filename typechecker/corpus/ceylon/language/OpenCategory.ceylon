shared interface OpenCategory<in X>
        satisfies Category
        given X satisfies Object {

    doc "Add the given objects to the category. Return the number of
         objects which did not already belong to the category."
    shared Natural add(X... objects);
}
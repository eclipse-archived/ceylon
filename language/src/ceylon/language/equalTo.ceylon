doc "Returns a partial function that will compare an element
     to any other element and returns true if they're equal.
     This is useful in conjunction with methods that receive
     a predicate function."
shared Boolean equalTo<Element>(Element val)(Element element)
        given Element satisfies Object 
                => element==val;

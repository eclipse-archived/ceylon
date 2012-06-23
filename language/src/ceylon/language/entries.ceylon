doc "An alias for `Integer->Element`."
shared class Indexed<Element>(Integer index, Element element)
        given Element satisfies Object 
        = Entry<Integer,Element>;

doc "Produces a sequence of each index to element `Entry` 
     for the given sequence of values."
shared Indexed<Element>[] entries<Element>(Element... elements) 
        given Element satisfies Object {
    variable value index:=0;
    return { for (element in elements) index++->element };
}

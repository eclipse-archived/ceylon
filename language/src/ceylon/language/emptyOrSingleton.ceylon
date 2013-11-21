"A singleton `Tuple` with the given element if the 
 given element is non-null, or `Empty` otherwise ."
see (`class Tuple`, `interface Empty`)
shared []|[Element] emptyOrSingleton<Element>(Element? element) 
        given Element satisfies Object {
    if (exists element) {
        return [element];
    }
    else {
        return [];
    }
}
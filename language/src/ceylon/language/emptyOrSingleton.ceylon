"A singleton [[Tuple]] with the given element if the given 
 element is non-null, or the [[empty sequence|Empty]] 
 otherwise ."
see (`class Tuple`, `interface Empty`)
shared []|[Element&Object] emptyOrSingleton<Element>
        (Element element) {
    if (exists element) {
        return [element];
    }
    else {
        return [];
    }
}
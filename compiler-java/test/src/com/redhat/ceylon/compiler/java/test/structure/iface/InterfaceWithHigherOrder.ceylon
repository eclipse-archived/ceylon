@noanno
interface InterfaceWithHigherOrder<Element> {
    
    shared formal Iterable<Element> source;
    
    shared default 
    Result fold<Result>(Result initial)(
        "The accumulating function that accepts an
         [[intermediate result|partial]], and the 
         [[next element|element]]."
        Result accumulating(Result partial, 
            Element element)) {
        variable value partial = initial;
        for (elem in source) {
            partial = accumulating(partial, elem);
        }
        return partial;
    }
}
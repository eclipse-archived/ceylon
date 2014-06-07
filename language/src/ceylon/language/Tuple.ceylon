"""A _tuple_ is a typed linked list. Each instance of 
   `Tuple` represents the value and type of a single link.
   The attributes `first` and `rest` allow us to retrieve a 
   value from the list without losing its static type 
   information.
   
       value point = Tuple(0.0, Tuple(0.0, Tuple("origin")));
       Float x = point.first;
       Float y = point.rest.first;
       String label = point.rest.rest.first;
   
   Usually, we abbreviate code involving tuples.
   
       [Float,Float,String] point = [0.0, 0.0, "origin"];
       Float x = point[0];
       Float y = point[1];
       String label = point[2];
   
   A list of types enclosed in brackets is an abbreviated 
   tuple type. An instance of `Tuple` may be constructed by 
   surrounding a value list in brackets:
   
       [String,String] words = ["hello", "world"];
   
   The index operator with a literal integer argument is a 
   shortcut for a chain of evaluations of `rest` and 
   `first`. For example, `point[1]` means `point.rest.first`.
   
   A _terminated_ tuple type is a tuple where the type of
   the last link in the chain is `Empty`. An _unterminated_ 
   tuple type is a tuple where the type of the last link
   in the chain is `Sequence` or `Sequential`. Thus, a 
   terminated tuple type has a length that is known
   statically. For an unterminated tuple type only a lower
   bound on its length is known statically.
   
   Here, `point` is an unterminated tuple:
   
       String[] labels = ... ;
       [Float,Float,String*] point = [0.0, 0.0, *labels];
       Float x = point[0];
       Float y = point[1];
       String? firstLabel = point[2];
       String[] allLabels = point[2...];"""
by ("Gavin")
shared final class Tuple<out Element, out First, out Rest=[]>
            (first, rest)
        extends Object()
        satisfies [Element+]
        given First satisfies Element
        given Rest satisfies Element[] {
    
    "The first element of this tuple. (The head of the 
     linked list.)"
    shared actual First first;
    
    "A tuple with the elements of this tuple, except for the
     first element. (The tail of the linked list.)"
    shared actual Rest rest;
    
    size => 1 + rest.size;
    
    shared actual Element? get(Integer index) {
        switch (index<=>0)
        case (smaller) { return null; }
        case (equal) { return first; }
        case (larger) { return rest[index-1]; }
    }
    
    shared actual Integer lastIndex {
        if (exists restLastIndex = rest.lastIndex) {
            return restLastIndex+1;
        }
        else {
            return 0;
        }
    }
    
    "The last element of this tuple."
    shared actual Element last {
        if (nonempty rest) {
            return rest.last;
        }
        else {
            return first;
        }
    }
    
    shared actual Element[] segment(Integer from, Integer length) {
        if(length <= 0){
            return [];
        }
        Integer realFrom = from < 0 then 0 else from;
        if (realFrom==0) {
            return length==1 then [first]
                else rest[0:length+realFrom-1]
                        .withLeading(first);
        }
        return rest[realFrom-1:length];
    }
    
    shared actual Element[] span(Integer from, Integer end) {
        if (from<0 && end<0) { return []; }
        Integer realFrom = from < 0 then 0 else from;
        Integer realEnd = end < 0 then 0 else end;
        return realFrom<=realEnd then this[from:realEnd-realFrom+1] 
                else this[realEnd:realFrom-realEnd+1].reversed.sequence();
    }
    
    spanTo(Integer to) => to<0 then [] else span(0, to);
    
    spanFrom(Integer from) => span(from, size);
    
    reversed => rest.reversed.withTrailing(first);
    
    "This tuple."
    shared actual Tuple<Element,First,Rest> clone() => this;
    
    shared actual Iterator<Element> iterator() {
        object iterator satisfies Iterator<Element> {
            variable Element[] current = outer;
            shared actual Element|Finished next() {
                if (nonempty c = current) {
                    current = c.rest;
                    return c.first;
                }
                else {
                    return finished;
                }
            } 
        }
        return iterator;
    }
    
    "Determine if the given value is an element of this
     tuple."
    shared actual Boolean contains(Object element) {
        if (exists first, first==element) {
            return true;
        }
        else {
            return element in rest;
        }
    }
    
    "Returns a new tuple that starts with the specified
     element, followed by the elements of this tuple."
    shared actual 
    Tuple<Element|Other,Other,Tuple<Element,First,Rest>> 
    withLeading<Other>(
            "The first element of the resulting tuple."
            Other element) => Tuple(element, this);
    
}

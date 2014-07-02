"Produces the [[stream|Iterable]] that results from repeated 
 application of the given [[function|next]] to the given 
 [[first]] element of the stream. The stream ends when the 
 given [[predicate function|where]] first evaluates to 
 `false`. If the predicate function never returns `false`, 
 the stream is infinite.
 
 For example:
 
     loop(0, 10.largerThan, 2.plus)
 
 produces the stream `{ 0, 2, 4, 6, 8 }`."
shared {Element*} loop<Element>(
        "The first element of the resulting stream."
        Element first,
        "The predicate function that returns `false` when 
         the loop should terminate."
        Boolean where(Element element),
        "The function that produces the next element of the
         stream, given the current element."
        Element next(Element element)) {
    value start = first;
    object iterable satisfies {Element+} {
        first => start;
        function nextElement(Element element) => next(element);
        shared actual Iterator<Element> iterator() {
            variable Element current = start;
            object iterator satisfies Iterator<Element> {
                shared actual Element|Finished next() {
                    if (where(current)) {
                        Element result = current;
                        current = nextElement(current);
                        return result;
                    }
                    else {
                        return finished;
                    }
                }
            }
            return iterator;
        }
    }
    return iterable;
}

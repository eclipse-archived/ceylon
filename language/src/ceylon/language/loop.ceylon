"Produces the [[stream|Iterable]] that results from repeated 
 application of the given [[function|next]] to the given 
 [[first]] element of the stream. The stream is infinite.
 
 For example:
 
     loop(0)(2.plus).takeWhile(10.largerThan)
 
 produces the stream `{ 0, 2, 4, 6, 8 }`."
shared {Element+} loop<Element>(
        "The first element of the resulting stream."
        Element first)(
        "The function that produces the next element of the
         stream, given the current element."
        Element next(Element element)) {
    value start = first;
    object iterable satisfies {Element+} {
        first => start;
        empty => false;
        shared actual Nothing size {
            "stream is infinite"
            assert(false);
        }
        function nextElement(Element element) => next(element);
        shared actual Iterator<Element> iterator() {
            variable Element current = start;
            object iterator satisfies Iterator<Element> {
                shared actual Element|Finished next() {
                    Element result = current;
                    current = nextElement(current);
                    return result;
                }
            }
            return iterator;
        }
    }
    return iterable;
}

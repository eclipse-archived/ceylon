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
        Element next(Element element))
    => let (start = first) 
    object satisfies {Element+} {
        first => start;
        empty => false;
        shared actual Nothing size {
            "stream is infinite"
            assert(false);
        }
        function nextElement(Element element) 
                => next(element);
        iterator()
                => object satisfies Iterator<Element> {
            variable Element current = start;
            shared actual Element next() {
                Element result = current;
                current = nextElement(current);
                return result;
            }
        };
    };

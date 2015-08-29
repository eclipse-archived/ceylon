"Produces the [[stream|Iterable]] that results from repeated
 application of the given [[function|next]] to the given
 [[first]] element of the stream, until the function first
 returns [[finished]]. If the given function never returns 
 `finished`, the resulting stream is infinite.

 For example:

     loop(0)(2.plus).takeWhile(10.largerThan)

 produces the stream `{ 0, 2, 4, 6, 8 }`."
shared {Element+} loop<Element>(
        "The first element of the resulting stream."
        Element first)(
        "The function that produces the next element of the
         stream, given the current element. The function may
         return [[finished]] to indicate the end of the 
         stream."
        Element|Finished next(Element element))
    => let (start = first)
    object satisfies {Element+} {
        first => start;
        empty => false;
        function nextElement(Element element)
                => next(element);
        iterator()
                => object satisfies Iterator<Element> {
            variable Element|Finished current = start;
            shared actual Element|Finished next() {
                if (!is Finished result = current) {
                    current = nextElement(result);
                    return result;
                }
                else {
                    return finished;
                }
            }
        };
    };

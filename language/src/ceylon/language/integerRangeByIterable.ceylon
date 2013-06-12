"Provides an optimized implementation of `Range<Integer>.iterator`. 
 This is necessary because we need reified generics in order to write 
 the optimized version in pure Ceylon."
native 
{Element+} integerRangeByIterable<Element>(Range<Element> range, Integer step)
        given Element satisfies Ordinal<Element> & Comparable<Element>;
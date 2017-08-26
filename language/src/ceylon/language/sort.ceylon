"Sort the given elements according to their 
 [[natural order|Comparable]], returning a new 
 [[sequence|Sequential]].
 
 Note that [[Iterable.sort]] may be used to sort any stream
 according to a given comparator function."
see (interface Comparable,
     function Iterable.sort)
tagged("Streams", "Comparisons")
shared [Element+] | []&Iterable<Element,Absent> 
sort<Element,Absent>(
        "The unsorted stream of elements."
        Iterable<Element,Absent> elements) 
        given Element satisfies Comparable<Element> 
        given Absent satisfies Null
        => elements.sort(increasing);
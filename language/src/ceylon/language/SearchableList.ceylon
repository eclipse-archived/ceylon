"A [[List]] which can be efficiently searched for 
 occurrences of a given element, or for inclusions of a 
 given sublist of elements. This interface provides 
 operations for finding:
 
 - _occurrences_ of a single value in the list, and
 - _inclusions_ of a given sublist of values in the list.
 
 Occurrences and inclusions are identified by a list index
 at which the value or sublist of values occurs in the list. 
 In the case of an inclusion, it is the index of the first 
 matching value from the sublist."
see (`class String`, `class Array`)
shared interface SearchableList<Element> 
        satisfies List<Element> {
    
    "Determines if the given [[value|element]] occurs at the 
     given index in this list."
    shared default 
    Boolean occursAt(
        "The index at which the value might occur."
        Integer index, 
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element)
            => let (elem = getFromFirst(index))
            if (exists element, exists elem) 
            then elem == element 
            else element exists == elem exists;
    
    "The indexes in this list at which the given 
     [[value|element]] occurs."
    shared default 
    {Integer*} occurrences(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            => { for (i in from:length) 
                 if (occursAt(i, element)) i };
    
    "Determines if the given [[value|element]] occurs as an 
     element of this list, at any index that falls within
     the segment `from:length` defined by the optional 
     [[starting index|from]] and [[length]]."
    shared default 
    Boolean occurs(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            => !occurrences(element, from, length).empty;
    
    "Count the indexes in this list at which the given 
     [[value|element]] occurs, that fall within the segment 
     `from:length` defined by the optional 
     [[starting index|from]] and [[length]]."
    shared default
    Integer countOccurrences(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            => occurrences(element, from, length).size;
    
    "The first index in this list at which the given 
     [[value|element]] occurs, that falls within the segment 
     `from:length` defined by the optional 
     [[starting index|from]] and [[length]]."
    shared default 
    Integer? firstOccurrence(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from)
            => occurrences(element, from, length).first;
    
    "The last index in this list at which the given 
     [[value|element]] occurs, that falls within the range 
     `size-length-from:length` defined by the optional 
     [[starting index|from]], interpreted as a reverse index 
     counting from the _end_ of the list, and [[length]]."
    shared default 
    Integer? lastOccurrence(
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element,
        "The smallest index to consider, interpreted as
         a reverse index counting from the _end_ of the 
         list, where `0` is the last element of the list, 
         and `size-1` is the first element of the list."
        Integer from = 0,
        "The number of indexes to consider."
        Integer length = size-from) {
        //TODO: refine reversed to return a SearchableList
        // => reversed.firstOccurrence(element, from, length)
        for (index in (size-length-from:length).reversed) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at the given index of this list."
    shared default 
    Boolean includesAt(
        "The index at which the [[sublist]] might occur."
        Integer index, 
        List<Element> sublist) {
        if (0 <= index <= size-sublist.size) {
            variable value i = index;
            for (element in sublist) {
                if (exists element) {
                    if (!occursAt(i, element)) {
                        return false;
                    }
                }
                else {
                    assert (is Element null);
                    if (!occursAt(i, null)) {
                        return false;
                    }
                }
                i++;
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    "The indexes in this list at which the given 
     [[list|sublist]] occurs as a sublist, that are greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    {Integer*} inclusions(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) 
            => { for (i in from:size+1-from-sublist.size) 
                 if (includesAt(i, sublist)) i };
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at some index in this list, at any index that 
     is greater than or equal to the optional 
     [[starting index|from]]."
    shared default 
    Boolean includes(List<Element> sublist,
        "The smallest index to consider."
        Integer from = 0) 
            => !inclusions(sublist, from).empty;
    
    "Count the indexes in this list at which the given 
     [[list|sublist]] occurs as a sublist, that are greater 
     than or equal to the optional [[starting index|from]]."
    shared default
    Integer countInclusions(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) 
            => inclusions(sublist, from).size;
    
    "The first index in this list at which the given 
     [[list|sublist]] occurs as a sublist, that is greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    Integer? firstInclusion(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0)
            => inclusions(sublist, from).first;
    
    "The last index in this list at which the given 
     [[list|sublist]] occurs as a sublist, that falls within 
     the range `0:size-from+1-sublist.size` defined by the 
     optional [[starting index|from]], interpreted as a 
     reverse index counting from the _end_ of the list."
    shared default 
    Integer? lastInclusion(List<Element> sublist,
        "The smallest index to consider, interpreted as
         a reverse index counting from the _end_ of the 
         list, where `0` is the last element of the list, 
         and `size-1` is the first element of the list."
        Integer from = 0) {
        //TODO: refine reversed to return a SearchableList
        // => reversed.firstInclusion(element, from, length)
        for (index in (0:size-from+1-sublist.size).reversed) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
}
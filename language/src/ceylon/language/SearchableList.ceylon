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
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at the given index of this list."
    shared default 
    Boolean includesAt(
        "The index at which the [[sublist]] might occur."
        Integer index, 
        List<Element> sublist) {
        value subsize = sublist.size;
        if (subsize>size-index) {
            return false;
        }
        if (subsize==0 && index==size) {
            return true;
        }
        for (i in 0:subsize) {
            value x = getFromFirst(index+i);
            value y = sublist.getFromFirst(i);
            if (exists x) {
                if (exists y) {
                    if (x!=y) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else if (exists y) {
                return false;
            }
        }
        else {
            return true;
        }
    }
    
    "Determine if the given [[list|sublist]] occurs as a 
     sublist at some index in this list, at any index that 
     is greater than or equal to the optional 
     [[starting index|from]]."
    shared default 
    Boolean includes(List<Element> sublist,
        "The smallest index to consider."
        Integer from = 0) {
        for (index in from:size-from+1-sublist.size) {
            if (includesAt(index,sublist)) {
                return true;
            }
        }
        return false;
    }
    
    "The indexes in this list at which the given 
     [[list|sublist]] occurs as a sublist, that are greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    {Integer*} inclusions(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) 
            => object satisfies {Integer*} {
        size => countInclusions(sublist, from);
        empty => includes(sublist, from);
        first => firstInclusion(sublist, from);
        last => if (exists index = lastInclusion(sublist)) 
        then (index>=from then index) 
        else null;
        iterator() => let (list = outer)
        object satisfies Iterator<Integer> {
            variable value index = from;
            shared actual Integer|Finished next() {
                if (exists next 
                    = list.firstInclusion(sublist, index)) {
                    index = next+1;
                    return next;
                }
                else {
                    return finished;
                }
            }
        };
    };
    
    "Count the indexes in this list at which the given 
     [[list|sublist]] occurs as a sublist, that are greater 
     than or equal to the optional [[starting index|from]]."
    shared default
    Integer countInclusions(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) {
        variable value count = 0;
        for (index in from:size-from+1-sublist.size) {
            if (includesAt(index,sublist)) {
                count++;
            }
        }
        return count;
    }
    
    "The first index in this list at which the given 
     [[list|sublist]] occurs as a sublist, that is greater 
     than or equal to the optional [[starting index|from]]."
    shared default 
    Integer? firstInclusion(List<Element> sublist,
        "The smallest index to consider." 
        Integer from = 0) {
        for (index in from:size-from+1-sublist.size) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
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
        for (index in (0:size-from+1-sublist.size).reversed) {
            if (includesAt(index,sublist)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    "Determines if the given [[value|element]] occurs at the 
     given index in this list."
    shared default 
    Boolean occursAt(
        "The index at which the value might occur."
        Integer index, 
        "The value. If null, it is considered to occur
         at any index in this list with a null element."
        Element element) {
        value elem = getFromFirst(index);
        if (exists element) {
            return if (exists elem) 
            then elem==element 
            else false;
        }
        else {
            return !elem exists;
        }
    }
    
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
        Integer length = size-from) {
        for (index in from:length) {
            if (occursAt(index,element)) {
                return true;
            }
        }
        else {
            return false;
        }
    }
    
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
            => object satisfies {Integer*} {
        size => countOccurrences(element, from, length);
        empty => occurs(element, from, length);
        first => firstOccurrence(element, from, length);
        last => if (length>0,
            exists index
                    = lastOccurrence(element, from+length-1)) 
        then (index>=from then index)
        else null;
        iterator() => let (list = outer)
        object satisfies Iterator<Integer> {
            variable value index = from;
            shared actual Integer|Finished next() {
                if (exists next 
                    = list.firstOccurrence {
                    element = element;
                    from = index;
                    length = length;
                }) {
                    index = next+1;
                    return next;
                }
                else {
                    return finished;
                }
            }
        };
    };
    
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
        Integer length = size-from) {
        variable value count = 0;
        for (index in from:length) {
            if (occursAt(index,element)) {
                count++;
            }
        }
        return count;
    }
    
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
        Integer length = size-from) {
        for (index in from:length) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
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
        for (index in (size-length-from:length).reversed) {
            if (occursAt(index,element)) {
                return index;
            }
        }
        else {
            return null;
        }
    }
    
    /*shared actual default 
     Boolean startsWith(List<> sublist) 
            => includesAt(0, sublist);
     
     shared actual default
     Boolean endsWith(List<> sublist) 
            => includesAt(size-sublist.size, sublist);*/
    
    
}
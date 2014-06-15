shared class SequenceBuilder<Element>(Integer initialSize=5) {
    
    "The storage for appended elements."
    variable Array<Element?>? store = null;
    
    "The number of items in [[array]] which have actually 
     been appended."
    variable Integer length = 0;
    
    "Resize policy."
    Integer newSize(Integer extra) {
        Integer existingSize;
        if (exists array=store) {
            existingSize = array.size;
        } else {
            existingSize = 0;
        }
        value requiredSize = length+extra;
        variable value result = existingSize*2+2;
        "Overflow"
        assert (requiredSize > 0);
        if (result < requiredSize) {
            result = requiredSize;
        }
        "Required array too large"
        assert (result <= runtime.maxArraySize);
        return result;
    }
    
    "Returns the storage array ready for storing [[extra]] 
     more elements. Reallocates and copies existing entries 
     if needed."
    Array<Element?> getStorage(Integer extra) {
        // extra should be > 0
        if (exists array=store) {
            if (array.size >= length + extra) {
                return array;
            } else {
                value arraySize = newSize(extra);
                value newArray 
                        = arrayOfSize<Element?>(arraySize, null);
                array.copyTo(newArray);
                this.store = newArray;
                return newArray;
            }
        } else {
            value arraySize = initialSize>extra 
                then initialSize else newSize(extra);
            value newArray 
                    = arrayOfSize<Element?>(arraySize, null);
            this.store = newArray;
            return newArray;
        }
    }
    
    
    "The resulting [[sequence|Sequential]]."
    shared Element[] sequence() {
        if (exists array=store, length>0) {
            if (is Array<Element> array) {
                //Element is a supertype of Null
                return ArraySequence(array[0:length]);
            } else {
                //Element is a subtype of Object
                assert (exists first = array.first);
                //TODO: make a more efficient way to copy
                //      a portion of the array
                value result 
                        = arrayOfSize<Element>(length, first);
                for (index in 1:length-1) {
                    assert (exists element 
                        = array.getFromFirst(index));
                    result.set(index, element);
                }
                return ArraySequence(result);
            }
        } else {
            return [];
        }
    }
    
    "Add the given [[element]]."
    shared SequenceBuilder<Element> append(Element element) {
        getStorage(1).set(length++, element);
        return this;
    }
    
    "Append all of the given [[elements]]."
    shared SequenceBuilder<Element> appendAll(
            "The elements to append."
            {Element*} elements) {
        if (is [Element+] elements) {
            value size = elements.size;
            if (size>0) {
                value array = getStorage(size);
                for (element in elements) {
                    array.set(length++, element);
                }
            }
        } else if (is Array<Element> elements) {
            value size = elements.size;
            if (size>0) {
                value array = getStorage(size);
                if (is Array<Element> array) {
                    //Element is a supertype of Null
                    elements.copyTo(array, 0, length, size);
                }
                else {
                    //Element is a supbtype of Object
                    for (index in 0:size) {
                        assert (exists element 
                            = elements.getFromFirst(index));
                        array.set(index, element);
                    }
                }
                length+=size;
            }
        }
        //TODO: what about a generic Collection?
        else {
            for (element in elements) {
                getStorage(1).set(length++, element);
            }
        }
        return this;
    }
    
    "Returns the `size` of the [[sequence]]."
    shared Integer size => length;
    
    "Get the element at the given [[index]] in the 
     [[sequence]]."
    shared Element? get(Integer index) {
        if (exists array=store, 0<=index<length) {
            return array.getFromFirst(index);
        } else {
            return null;
        }
    }
    
}

class ArraySequence<Element>(Array<Element> array) 
        extends Object()
        satisfies [Element+] {
    
    assert (!array.empty);
    
    shared actual Element? getFromFirst(Integer index)
            => array.getFromFirst(index);
    
    shared actual Boolean contains(Object element) 
            => array.contains(element);
    
    shared actual Integer size => array.size;
    
    shared actual Iterator<Element> iterator() 
            => array.iterator();
    
    shared actual Element first {
        assert (is Element first = array.first);
        return first;
    }
    
    shared actual Element last {
        assert (is Element last = array.last);
        return last;
    }
    
    shared actual Integer lastIndex {
        assert (exists lastIndex = array.lastIndex);
        return lastIndex;
    }
    
    shared actual Element[] rest 
            => size==1 then [] else ArraySequence(Array(array.rest));
    
    shared actual [Element+] reversed 
            => ArraySequence(Array(array.reversed));
    
    shared actual Element[] segment(Integer from, Integer length) {
        if (from>lastIndex || length<=0 || from+length<=0) {
            return [];
        }
        else if (from < 0) {
            return ArraySequence(array[0:length+from]);
        } else {
            return ArraySequence(array[from:length]);
        }
    }
    
    shared actual Element[] span(Integer from, Integer to) {
        if (from <= to) {
            if (to < 0 || from > lastIndex) {
                return [];
            }
            return ArraySequence(array[largest(from, 0)..smallest(to, lastIndex)]);
        } else {
            if (from < 0 || to > lastIndex) {
                return [];
            }
            return ArraySequence(array[smallest(from, lastIndex)..largest(to, 0)]);
        }
    }
    
    shared actual Element[] spanFrom(Integer from) {
        if (from > lastIndex) {
            return [];
        } else {
            return ArraySequence(array[largest(from, 0)...]);
        }
    }
    
    shared actual Element[] spanTo(Integer to) {
        if (to < 0) {
            return [];
        } else { 
            return ArraySequence(array[...smallest(to, lastIndex)]);
        }
    }
    
}
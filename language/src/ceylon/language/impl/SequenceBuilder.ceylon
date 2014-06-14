shared class SequenceBuilder<Element>(Integer initialSize=5) {
    
    "The storage for appended elements."
    variable Array<Element>? store = null;
    "The number of items in [[array]] which have actually 
     been appended."
    variable Integer length = 0;
    
    "Resize policy."
    Integer newSize(Integer existingSize, Integer extra) {
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
    Array<Element> getStorage(Integer extra, Element example) {
        // extra should be > 0
        if (exists array=store) {
            if (array.size >= length + extra) {
                return array;
            } else {
                value arraySize = newSize(array.size, extra);
                value newArray 
                        = arrayOfSize(arraySize, example);
                array.copyTo(newArray);
                this.store = newArray;
                return newArray;
            }
        } else {
            value arraySize = initialSize>extra 
                then initialSize else newSize(0, extra);
            value newArray 
                    = arrayOfSize(arraySize, example);
            this.store = newArray;
            return newArray;
        }
    }
    
    
    "The resulting [[sequence|Sequential]]."
    shared Element[] sequence() {
        if (exists array=store) {
            return array.take(length).sequence();
        } else {
            return [];
        }
    }
    
    "Append the given [[element]]."
    shared SequenceBuilder<Element> append(Element element) {
        getStorage(1, element).set(length++, element);
        return this;
    }
    
    "Append all of the given [[elements]]."
    shared SequenceBuilder<Element> appendAll({Element*} elements) {
        if (is [Element+] elements) {
            value size = elements.size;
            if (size>0) {
                value array = getStorage(size, elements.first);
                for (element in elements) {
                    array.set(length++, element);
                }
            }
        }
        else if (is Array<Element> elements) {
            value size = elements.size;
            if (size>0) {
                Array<Element> array;
                if (exists example = elements.first) {
                    array = getStorage(size, example);
                }
                else {
                    assert (is Element null);
                    array = getStorage(size, null);
                }
                elements.copyTo(array, 0, length, size);
                length+=size;
            }
        }
        //TODO: what about a generic Collection?
        else {
            for (element in elements) {
                getStorage(1, element).set(length++, element);
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
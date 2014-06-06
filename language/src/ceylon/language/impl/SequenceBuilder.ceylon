shared class SequenceBuilder<Element>(Integer initialSize=5) {
    "The storage"
    variable Array<Element>? store = null;
    "The number of items in [[array]] which have actually been appended."
    variable Integer length = 0;
    
    "Resize policy"
    Integer newSize(
        Integer existingSize, Integer extra) {
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
    
    "Returns the storage array ready for storing [[extra]] more elements.
     Reallocates and copies existing entries if needed."
    Array<Element> getStorage(Integer extra, Element example) {
        // extra should be > 0
        if (exists array=store) {
            if (array.size >= length + extra) {
                return array;
            } else {
                value newArray = arrayOfSize<Element>(newSize(array.size, extra), example);
                array.copyTo(newArray);
                this.store = newArray;
                return newArray;
            }
        } else {
            value newArray = arrayOfSize<Element>(initialSize, example);
            this.store = newArray;
            return newArray;
        }
    }
    
    
    "The resulting sequential."
    shared Element[] sequence {
        if (exists array=store) {
            return array.take(length).sequence;
        } else {
            return [];
        }
    }
    
    "Append the given [[element]]."
    shared SequenceBuilder<Element> append(Element element) {
        value store = getStorage(1, element);
        store.set(length, element);
        length++;
        return this;
    }
    
    "Append all of the given elements."
    shared SequenceBuilder<Element> appendAll({Element*} elements) {
        for (element in elements) {
            append(element);
        }
        return this;
    }
    
    "Returns the length of the current content"
    shared Integer size {
        return length;
    }
    
}
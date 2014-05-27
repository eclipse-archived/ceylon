"Crappy way to build a sequence now that 
 `ceylon.language::SequenceBuilder` has gone"
class ArrayBuilder<Element>() {
    
    "The storage"
    variable Array<Element>? storage = null;
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
        if (exists s=storage) {
            if (s.size >= length + extra) {
                return s;
            } else {
                value newStorage = arrayOfSize<Element>(newSize(s.size, extra), example);
                s.copyTo(newStorage);
                storage = newStorage;
                return newStorage;
            }
        } else {
            value newStorage = arrayOfSize<Element>(newSize(0, extra), example);
            storage = newStorage;
            return newStorage;
        }
    }
    
    shared ArrayBuilder<Element> append(Element element) {
        value store = getStorage(1, element);
        store.set(length, element);
        length += 1;
        return this;
    }
    
    shared ArrayBuilder<Element> appendAll({Element*} elements) {
        // TODO If we know the size of elements efficiently we can just do one allocation.
        for (element in elements) {
            append(element);
        }
        return this;
    }
    shared actual String string {
        return sequence.string;
    }
    
    shared Element[] sequence {
        if (exists s=storage) {
            return s.take(length).sequence;
        }
        return empty;
    }
    
    //shared Integer size => length;
    
}

"Builder utility for constructing [[strings|String]] by 
 incrementally appending strings or characters"
shared class StringBuilder() {
    "The storage"
    variable Array<Character> array 
            = arrayOfSize<Character>(10, 'X');
    "The number of items in [[array]] which have actually 
     been appended."
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
    
    "Returns the storage array ready for storing [[extra]] 
     more elements. Reallocates and copies existing entries 
     if needed."
    Array<Character> getStorage(Integer extra) {
        // extra should be > 0
        if (array.size >= length + extra) {
            return array;
        } else {
            value newArray 
                    = arrayOfSize<Character>
                        (newSize(array.size, extra), 'X');
            array.copyTo(newArray);
            array = newArray;
            return newArray;
        }
    }
    
    
    "The resulting string. If no characters have been
     appended, the empty string."
    shared actual String string {
        return String(array.take(length));
    }
    
    "Append the characters in the given [[string]]."
    shared StringBuilder append(String string) {
        if (!string.empty) {
            value store = getStorage(string.size);
            variable value i = 0;
            while (i < string.size) {
                assert(exists char=string[i]);
                store.set(length+i, char);
                i++;
            }
            length += string.size;
        }
        return this;
    }
    
    "Append the characters in the given [[strings]]."
    shared StringBuilder appendAll({String*} strings) {
        for (s in strings) {
            append(s);
        }
        return this;
    }
    
    "Append the given [[character]]."
    shared StringBuilder appendCharacter(Character character) {
        value store = getStorage(1);
        store.set(length, character);
        length++;
        return this;
    }
    
    "Append a newline character."
    shared StringBuilder appendNewline() {
        appendCharacter('\n');
        return this;
    }
    
    "Append a space character."
    shared StringBuilder appendSpace() {
        appendCharacter(' ');
        return this;
    }
    
    "Remove all content and return to initial state."
    shared StringBuilder reset() {
        // TODO The sequence version is called deleteAll
        length = 0;
        return this;
    }
    
    "Insert a [[string]] at the specified [[index]]. If the 
     `index` is beyond the end of the current string, the 
     new content is simply appended to the current content. 
     If the `index` is a negative number, the new content is
     inserted at index 0."
    shared StringBuilder insert(Integer index, 
        String string) {
        if (!string.empty) {
            value store = getStorage(string.size);
            // make the gap
            store.copyTo(store, index, 
                index+string.size, 
                this.length-index);
            // copy into it
            variable value i = 0;
            while (i < string.size) {
                assert(exists char = string[i]);
                store.set(index+i, char);
                i++;
            }
            length+= string.size;
        }
        return this;
    }
    
    "Insert a [[character]] at the specified [[index]]. If 
     the `index` is beyond the end of the current string, 
     the new content is simply appended to the current 
     content. If the `index` is a negative number, the new 
     content is inserted at index 0."
    shared StringBuilder insertCharacter(Integer index, 
        Character character) {
        return insert(index, character.string);
    }
    
    "Deletes the specified [[number of characters|length]] 
     from the current content, starting at the specified 
     [[index]]. If the `index` is beyond the end of the 
     current content, nothing is deleted. If the number of 
     characters to delete is greater than the available 
     characters from the given `index`, the content is 
     truncated at the given `index`. If `length` is 
     nonpositive, nothing is deleted."
    shared StringBuilder delete(variable Integer index, 
            Integer length=1) {
        if (index < 0) {
            index = 0;
        } else if (index >= this.length) {
            return this;
        }
        value srcPos = smallest(index+length, this.length);
        array.copyTo(array, srcPos, index, this.length-srcPos);
        if (index+length > this.length) {
            this.length = index;
        } else {
            this.length -= length;
        }
        
        return this;
    }
    
    "Deletes the specified [[number of characters|length]] 
     from the start of the string. If `length` is 
     nonpositive, nothing is deleted."
    shared StringBuilder deleteInitial(Integer length) 
            => delete(0, length);
    
    "Deletes the specified [[number of characters|length]] 
     from the end of the string. If `length` is nonpositive, 
     nothing is deleted."
    shared StringBuilder deleteTerminal(Integer length) 
            => delete(size-length, length);
    
    "Returns the length of the current content, that is,
     the [[size|String.size]] of the produced [[string]]."
    shared Integer size => length;
    
}
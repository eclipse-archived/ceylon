"Since strings are immutable, this class is used for
 constructing a string by incrementally appending 
 characters to the empty string. This class is mutable 
 but threadsafe."
shared native class StringBuilder() {
    
    "The resulting string. If no characters have been
     appended, the empty string."
    shared actual native String string;
    
    "Append the characters in the given string."
    shared native StringBuilder append(String string);
    
    "Append the characters in the given strings."
    shared StringBuilder appendAll({String*} strings) {
        for (s in strings) {
            append(s);
        }
        return this;
    }
    
    "Append the given character."
    shared StringBuilder appendCharacter(Character character) {
        append(character.string);
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
    shared native StringBuilder reset();

    "Insert a `String` at the specified position. If the 
     position is beyond the end of the current string, 
         the new content is simply appended to the current 
         content. If the position is a negative number, the 
         new content is inserted at index 0."
    shared native StringBuilder insert(Integer index, 
            String string);

    "Insert a `Character` at the specified position. If 
     the position is beyond the end of the current string, 
     the new content is simply appended to the current 
     content. If the position is a negative number, the 
     new content is inserted at index 0."
    shared native StringBuilder insertCharacter(Integer index, 
            Character character);

    "Deletes the specified number of characters from the
     current content, starting at the specified position.
     If the position is beyond the end of the current 
     content, nothing is deleted. If the number of 
     characters to delete is greater than the available 
     characters from the given position, the content is 
     truncated at the given position."
    shared native StringBuilder delete(Integer index, Integer length);

    "Returns the size of the current content."
    shared native Integer size;

}
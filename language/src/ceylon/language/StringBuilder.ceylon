doc "Since strings are immutable, this class is used for
     constructing a string by incrementally appending 
     characters to the empty string. This class is mutable 
     but threadsafe."
shared class StringBuilder() {
    doc "The resulting string. If no characters have been
         appended, the empty string."
    shared actual String string { 
        throw; 
    }
    doc "Append the characters in the given string."
    shared StringBuilder append(String string) {
        throw;
    }
    doc "Append the characters in the given strings."
    shared StringBuilder appendAll(String* strings) {
        for (s in strings) {
            append(s);
        }
        return this;
    }
    doc "Append the given character."
    shared StringBuilder appendCharacter(Character character) {
        append(character.string);
        return this;
    }
    doc "Append a newline character."
    shared StringBuilder appendNewline() {
        appendCharacter(`\n`);
        return this;
    }
    doc "Append a space character."
    shared StringBuilder appendSpace() {
        appendCharacter(` `);
        return this;
    }

    doc "Remove all content and return to initial state."
    shared StringBuilder reset() {
        throw;
    }

    doc "Insert a String or Character at the specified position.
         If the position is beyond the end of the current
         string, the new content is simply appended to the
         current content. If the position is a negative number,
         the new content is inserted at index 0."
    shared StringBuilder insert(Integer pos, Character|String content) {
        throw;
    }

    doc "Deletes the specified number of characters from the
         current content, starting at the specified position.
         If the position is beyond the end of the current content,
         nothing is deleted. If the number of characters to delete
         is greater than the available characters from the given
         position, the content is truncated at the given position."
    shared StringBuilder delete(Integer pos, Integer count) {
        throw;
    }

    doc "Returns the size of the current content."
    shared Integer size {
        throw;
    }

}
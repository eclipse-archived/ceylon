import java.lang { JStringBuilder=StringBuilder, JCharacter=Character { toChars, charCount } }

"""Builder utility for constructing [[strings|String]] by 
   incrementally appending strings or characters.
   
       value builder = StringBuilder();
       builder.append("hello");
       builder.appendCharacter(' ');
       builder.append("world");
       String hello = builder.string; //hello world"""
shared native final class StringBuilder() 
        satisfies List<Character> {
    
    "The number characters in the current content, that is, 
     the [[size|String.size]] of the produced [[string]]."
    shared actual native Integer size;
    
    shared actual native Integer? lastIndex;
    
    "The resulting string. If no characters have been
     appended, the empty string."
    shared actual native String string;
    
    shared actual native Iterator<Character> iterator();
    
    "Returns a string of the given [[length]] containing
     the characters beginning at the given [[index]]."
    shared native 
    String substring(Integer index, Integer length);
    
    shared actual native
    Character? getFromFirst(Integer index);
    
    "Append the characters in the given [[string]]."
    shared native 
    StringBuilder append(String string);
    
    "Append the characters in the given [[strings]]."
    shared native 
    StringBuilder appendAll({String*} strings) {
        for (s in strings) {
            append(s);
        }
        return this;
    }
    
    "Prepend the characters in the given [[string]]."
    shared native 
    StringBuilder prepend(String string);
    
    "Prepend the characters in the given [[strings]]."
    shared native 
    StringBuilder prependAll({String*} strings) {
        for (s in strings) {
            prepend(s);
        }
        return this;
    }
    
    "Append the given [[character]]."
    shared native 
    StringBuilder appendCharacter(Character character);
    
    "Prepend the given [[character]]."
    shared native 
    StringBuilder prependCharacter(Character character);
    
    "Append a newline character."
    shared native 
    StringBuilder appendNewline() => appendCharacter('\n');
    
    "Append a space character."
    shared native 
    StringBuilder appendSpace() => appendCharacter(' ');
    
    "Remove all content and return to initial state."
    shared native 
    StringBuilder clear();
    
    "Insert a [[string]] at the specified [[index]]."
    shared native 
    StringBuilder insert(Integer index, String string);
    
    "Insert a [[character]] at the specified [[index]]."
    shared native 
    StringBuilder insertCharacter
    (Integer index, Character character) ;
    
    "Replaces the specified [[number of characters|length]] 
     from the current content, starting at the specified 
     [[index]], with the given [[string]]. If `length` is 
     nonpositive, nothing is replaced."
    shared native 
    StringBuilder replace
    (Integer index, Integer length, String string);
    
    "Deletes the specified [[number of characters|length]] 
     from the current content, starting at the specified 
     [[index]]. If `length` is nonpositive, nothing is 
     deleted."
    shared native 
    StringBuilder delete(Integer index, Integer length/*=1*/);
    
    "Deletes the specified [[number of characters|length]] 
     from the start of the string. If `length` is 
     nonpositive, nothing is deleted."
    shared native 
    StringBuilder deleteInitial(Integer length);
    
    "Deletes the specified [[number of characters|length]] 
     from the end of the string. If `length` is nonpositive, 
     nothing is deleted."
    shared native 
    StringBuilder deleteTerminal(Integer length);
    
    "Reverses the order of the current characters."
    shared native 
    StringBuilder reverseInPlace();
    
    shared actual native Boolean equals(Object that);
    shared actual native Integer hash;
}

shared native("jvm") final class StringBuilder() 
        satisfies List<Character> {
    
    JStringBuilder builder = JStringBuilder();
    
    shared actual native("jvm") Integer size =>
            builder.codePointCount(0, builder.length());
    
    shared actual native("jvm") Integer? lastIndex =>
            if (builder.length() == 0)
    then null
    else builder.length() - 1;
    
    shared actual native("jvm") String string =>
            builder.string;
    
    shared actual native("jvm") Iterator<Character> iterator() {
        object stringBuilderIterator
                satisfies Iterator<Character> {
            variable Integer offset = 0;
            shared actual Character|Finished next() {
                if (offset < builder.length()) {
                    Integer codePoint = builder.codePointAt(offset);
                    offset += charCount(codePoint);
                    return codePoint.character;
                }
                else {
                    return finished;
                }
            }
        }
        return stringBuilderIterator;
    }
    
    shared native("jvm") 
    String substring(Integer index, Integer length) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            Integer start = startIndex(index);
            Integer end = endIndex(start, length);
            return builder.substring(start, end);
        }
        else {
            return "";
        }
    }
    
    shared actual native("jvm")
    Character? getFromFirst(Integer index) =>
            if (index<0 || index>size)
    then null
    else builder.codePointAt(startIndex(index)).character;
    
    shared native("jvm") 
    StringBuilder append(String string) {
        builder.append(string);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder prepend(String string) {
        builder.insert(0, string);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder appendCharacter(Character character) {
        builder.appendCodePoint(character.integer);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder prependCharacter(Character character) {
        builder.insert(0, toChars(character.integer));
        return this;
    }
    
    shared native("jvm") 
    StringBuilder clear() {
        builder.setLength(0);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder insert(Integer index, String string) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else {
            builder.insert(startIndex(index), string);
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder insertCharacter
    (Integer index, Character character) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else {
            builder.insert(startIndex(index),
                toChars(character.integer));
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder replace
    (Integer index, Integer length, String string) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (!string.empty) {
            Integer len = (length<0) then 0 else length;
            Integer start = startIndex(index);
            Integer end = endIndex(start, len);
            builder.replace(start, end, string);
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder delete(Integer index, Integer length) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            Integer start = startIndex(index);
            Integer end = endIndex(start, length);
            builder.delete(start, end);
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder deleteInitial(Integer length) {
        if (length>size) {
            throw AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            builder.delete(0, startIndex(length));
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder deleteTerminal(Integer length) {
        if (length>size) {
            throw AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            Integer start = startIndex(size - length);
            builder.delete(start, builder.length());
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder reverseInPlace() {
        builder.reverse();
        return this;
    }
    
    shared actual native("jvm") Boolean equals(Object that) {
        return builder.equals(that);
    }
    
    shared actual native("jvm") Integer hash {
        return builder.hash;
    }
    
    Integer startIndex(Integer index) {
        return builder.offsetByCodePoints(0, index);
    }
    
    Integer endIndex(Integer start, Integer length) {
        return builder.offsetByCodePoints(start, length);
    }
    
}

shared native("js") final class StringBuilder() 
        satisfies List<Character> {
    
    variable String str = "";
    
    shared actual native("js") Integer size =>
            str.size;
    
    shared actual native("js") Integer? lastIndex =>
            if (str.size == 0)
    then null
    else str.size - 1;
    
    shared actual native("js") String string => str;
    
    shared actual native("js") Iterator<Character> iterator() =>
            str.iterator();
    
    shared native("js") 
    String substring(Integer index, Integer length) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            return str[index..length];
        }
        else {
            return "";
        }
    }
    
    shared actual native("js")
    Character? getFromFirst(Integer index) {
        if (index<0 || index>size) {
            return null;
        }
        else {
            return str.getFromFirst(index);
        }
    }
    
    shared native("js") 
    StringBuilder append(String string) {
        str = str + string;
        return this;
    }
    
    shared native("js") 
    StringBuilder prepend(String string) {
        str = string + str;
        return this;
    }
    
    shared native("js") 
    StringBuilder appendCharacter(Character character) {
        str = str + character.string;
        return this;
    }
    
    shared native("js") 
    StringBuilder prependCharacter(Character character) {
        str = character.string + str;
        return this;
    }
    
    shared native("js") 
    StringBuilder clear() {
        str = "";
        return this;
    }
    
    shared native("js") 
    StringBuilder insert(Integer index, String string) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else {
            str = str[0:index] + string + str[index...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder insertCharacter(Integer index, Character character) {
        return insert(index, character.string);
    }
    
    shared native("js") 
    StringBuilder replace(Integer index, Integer length, String string) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (!string.empty) {
            str = str[0:index] + string + str[index+length...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder delete(Integer index, Integer length) {
        if (index<0) {
            throw AssertionError("index must not be negative");
        }
        else if (index>size) {
            throw AssertionError("index must not be greater than size");
        }
        else if (index+length>size) {
            throw AssertionError("index+length must not be greater than size");
        }
        else if (length>0) {
            str = str[0:index] + str[index+length...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder deleteInitial(Integer length) {
        if (length>size) {
            throw AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            str = str[length...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder deleteTerminal(Integer length) {
        if (length>size) {
            throw AssertionError("length must not be greater than size");
        }
        else if (length>0) {
            str = str[0:size-length];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder reverseInPlace() {
        str = str.reversed;
        return this;
    }
    
    shared actual native("js") Boolean equals(Object that) {
        return str.equals(that);
    }
    
    shared actual native("js") Integer hash {
        return str.hash;
    }
}

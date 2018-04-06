/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
"""Builder utility for constructing [[strings|String]] by 
   incrementally appending strings or characters.
   
       value builder = StringBuilder();
       builder.append("hello");
       builder.appendCharacter(' ');
       builder.append("world");
       String hello = builder.string; //hello world"""
tagged("Strings")
shared native final class StringBuilder() 
        satisfies SearchableList<Character> &
                  Ranged<Integer,Character,String> &
                  IndexedCorrespondenceMutator<Character> { 
    
    "The number of characters in the current content, that 
     is, the [[size|String.size]] of the produced [[string]]."
    shared actual native Integer size;
    
    "Determines if the current content holds at least one
     character."
    shared actual native Boolean empty;
    
    shared actual native Integer? lastIndex;
    
    "The resulting string. If no characters have been
     appended, the empty string."
    shared actual native variable String string;
    
    "A copy of this `StringBuilder`, whose content is 
     initially the same as the current content of this
     instance."
    since("1.3.0")
    shared actual StringBuilder clone() {
        value clone = StringBuilder();
        clone.string = string;
        return clone;
    }
    
    shared actual native Iterator<Character> iterator();
    
    "Returns a string of the given [[length]] containing
     the characters beginning at the given [[index]]."
    deprecated ("use [[measure]]")
    since("1.1.0")
    shared 
    String substring(Integer index, Integer length)
            => measure(index, length);
    
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
    since("1.1.0")
    shared native 
    StringBuilder prepend(String string);
    
    "Prepend the characters in the given [[strings]]."
    since("1.1.0")
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
    since("1.1.0")
    shared native 
    StringBuilder prependCharacter(Character character);
    
    "Append a newline character."
    shared native 
    StringBuilder appendNewline() => appendCharacter('\n');
    
    "Append a space character."
    shared native 
    StringBuilder appendSpace() => appendCharacter(' ');
    
    "Remove all content and return to initial state."
    since("1.1.0")
    shared native 
    StringBuilder clear();
    
    "Set the character at the given index to the given
     [[character]]."
    since("1.3.0")
    shared actual void set(Integer index, Character character)
            => replace(index, 1, character.string);
    
    "Insert a [[string]] at the specified [[index]]."
    shared native 
    StringBuilder insert(Integer index, String string);
    
    "Insert a [[character]] at the specified [[index]]."
    shared native 
    StringBuilder insertCharacter
            (Integer index, Character character);
    
    "Replaces the specified [[number of characters|length]] 
     from the current content, starting at the specified 
     [[index]], with the given [[string]]. If [[length]] is 
     nonpositive, nothing is replaced, and the `string` is
     simply inserted at the specified `index`."
    since("1.1.0")
    shared native 
    StringBuilder replace
            (Integer index, Integer length, String string);
    
    "Deletes the specified [[number of characters|length]] 
     from the current content, starting at the specified 
     [[index]]. If [[length]] is nonpositive, nothing is 
     deleted."
    shared native 
    StringBuilder delete(Integer index, Integer length/*=1*/);
    
    "Deletes the specified [[number of characters|length]] 
     from the start of the string. If `length` is 
     nonpositive, nothing is deleted."
    since("1.1.0")
    shared native 
    StringBuilder deleteInitial(Integer length);
    
    "Deletes the specified [[number of characters|length]] 
     from the end of the string. If `length` is nonpositive, 
     nothing is deleted."
    since("1.1.0")
    shared native 
    StringBuilder deleteTerminal(Integer length);
    
    "Reverses the order of the current characters."
    since("1.1.0")
    shared native 
    StringBuilder reverseInPlace();
    
    "The first index at which the given 
     [[list of characters|sublist]] occurs as a sublist, 
     that is greater than or equal to the optional 
     [[starting index|from]]."
    shared actual native
    Integer? firstInclusion(List<Character> sublist,
        Integer from);
    
    "The last index at which the given 
     [[list of characters|sublist]] occurs as a sublist, 
     that falls within the range `0:size-from+1-sublist.size` 
     defined by the optional [[starting index|from]], 
     interpreted as a reverse index counting from the _end_
     of the list."
    shared actual native
    Integer? lastInclusion(List<Character> sublist,
        Integer from);
    
    "The first index at which the given [[character]] occurs, 
     that is greater than or equal to the optional 
     [[starting index|from]]."
    shared actual native
    Integer? firstOccurrence(Character character,
        Integer from, Integer length);
    
    "The last index at which the given [[character]] occurs, 
     that falls within the range `0:size-from` defined by 
     the optional [[starting index|from]], interpreted as a 
     reverse index counting from the _end_ of the list."
    shared actual native
    Integer? lastOccurrence(Character character,
        Integer from, Integer length);
    
    shared actual native
    {Integer*} inclusions(List<Character> sublist, 
        Integer from);
    
    shared actual native
    {Integer*} occurrences(Character character, 
        Integer from, Integer length);
    
    shared actual 
    Boolean occursAt(Integer index, Character character) 
            => if (exists ch = getFromFirst(index))
                then ch == character else false;
    
    shared actual 
    Boolean includesAt(Integer index, List<Character> sublist)
            => this[index:sublist.size] == sublist;
    
    shared actual native 
    String measure(Integer from, Integer length);
    
    shared actual 
    String initial(Integer length) 
            => measure(0, length);
    
    shared actual 
    String terminal(Integer length) 
            => measure(size-length, length);
    
    shared actual native String span(Integer from, Integer to);
    shared actual native String spanTo(Integer to);
    shared actual native String spanFrom(Integer from);
    
    shared actual Boolean equals(Object that) 
            => (super of List<Character>).equals(that);
    shared actual Integer hash
            => (super of List<Character>).hash;
}

shared native("jvm") final class StringBuilder() 
        satisfies SearchableList<Character> &
                  Ranged<Integer,Character,String> &
                  IndexedCorrespondenceMutator<Character> {
    
    import java.lang {
        JStringBuilder=StringBuilder,
        JCharacter=Character {
            toChars,
            charCount
        },
        IndexOutOfBoundsException
    }
    
    value builder = JStringBuilder();
    
    shared actual native("jvm") Integer size 
            => builder.codePointCount(0, builder.length());
    
    shared actual native("jvm") Boolean empty
            => builder.length() == 0;
    
    shared actual native("jvm") Integer? lastIndex 
            => if (builder.length() == 0)
            then null
            else size - 1;
    
    shared actual native("jvm") String string 
            => builder.string;
    
    native("jvm") assign string 
            => builder.replace(0, builder.length(), string);
    
    shared actual native("jvm") 
    Iterator<Character> iterator() {
        object stringBuilderIterator
                satisfies Iterator<Character> {
            variable Integer offset = 0;
            shared actual Character|Finished next() {
                if (offset < builder.length()) {
                    Integer codePoint = 
                            builder.codePointAt(offset);
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
    
    shared actual native("jvm")
    Character? getFromFirst(Integer index) {
        try {
            return builder.codePointAt(startIndex(index))
                          .character;
        }
        catch (IndexOutOfBoundsException ioobe) {
            return null;
        }
    }
    
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
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        builder.insert(startIndex(index), string);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder insertCharacter
            (Integer index, Character character) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        builder.insert(startIndex(index),
            toChars(character.integer));
        return this;
    }
    
    shared native("jvm") 
    StringBuilder replace
            (Integer index, Integer length, String string) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        "index+length must not be greater than size"
        assert (index+length<=size);
        Integer len = length<0 then 0 else length;
        Integer start = startIndex(index);
        Integer end = endIndex(start, len);
        builder.replace(start, end, string);
        return this;
    }
    
    shared native("jvm") 
    StringBuilder delete(Integer index, Integer length) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        "index+length must not be greater than size"
        assert (index+length<=size);
        if (length>0) {
            Integer start = startIndex(index);
            Integer end = endIndex(start, length);
            builder.delete(start, end);
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder deleteInitial(Integer length) {
        "length must not be greater than size"
        assert (length<=size);
        if (length>0) {
            builder.delete(0, startIndex(length));
        }
        return this;
    }
    
    shared native("jvm") 
    StringBuilder deleteTerminal(Integer length) {
        "length must not be greater than size"
        assert (length<=size);
        if (length>0) {
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
    
    shared actual native("jvm")
    Integer? firstInclusion(List<Character> sublist,
        Integer from) {
        try {
            value start 
                    = builder.offsetByCodePoints(0, 
                            from>0 then from else 0);
            value index 
                    = builder.indexOf(String(sublist), 
                            start);
            return index>=0 
                then from 
                    + builder.codePointCount(start, index);
        }
        catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }
    
    shared actual native("jvm")
    Integer? lastInclusion(List<Character> sublist,
        Integer from) {
        try {
            value start 
                    = builder.offsetByCodePoints(
                            builder.length(), 
                            (from>0 then -from else 0)
                                - sublist.size);
            value index 
                    = builder.lastIndexOf(String(sublist), 
                            start);
            return index>=0 
                then builder.codePointCount(0, index);
        }
        catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }
    
    shared actual native("jvm")
    Integer? firstOccurrence(Character character,
        Integer from, Integer length) {
        if (length<=0) { return null; }
        try {
            value start 
                    = builder.offsetByCodePoints(0, 
                            from>0 then from else 0);
            value index 
                    = builder.indexOf(character.string, 
                            start);
            if (index>=0) {
                value count  //TODO: wrong if from<0
                        = builder.codePointCount(start, index);
                return count < length
                    then from + count 
                    else null;
            }
            else {
                return null;
            }
        }
        catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }
    
    shared actual native("jvm")
    Integer? lastOccurrence(Character character,
        Integer from, Integer length) {
        if (length<=0) { return null; }
        try {
            value start 
                    = builder.offsetByCodePoints(
                            builder.length(), 
                            (from>0 then -from else 0) - 1);
            value index 
                    = builder.lastIndexOf(character.string, 
                            start);
            if (index>=0) {
                value count = //TODO: wrong if from<0
                        builder.codePointCount(index, start);
                return count < length 
                    then builder.codePointCount(0, index)
                    else null;
            }
            else {
                return null;
            }
        }
        catch (IndexOutOfBoundsException ioe) {
            return null;
        }
    }
    
    shared actual native("jvm")
    {Integer*} inclusions(List<Character> sublist, 
        Integer from)
            //TODO: optimize this!
            => string.inclusions(sublist, from);
    
    shared actual native("jvm")
    {Integer*} occurrences(Character character, 
        Integer from, Integer length)
            //TODO: optimize this!
            => string.occurrences(character, from, length);
    
    shared actual native("jvm")
    String measure(Integer from, Integer length) {
        value len = size;
        if (from >= len || length <= 0) {
            return "";
        }
        value resultLength 
                = if (from + length > len) 
                then len - from 
                else length;
        value start = startIndex(from);
        value end = endIndex(start, resultLength);
        return builder.substring(start, end);
    }
    
    shared actual native("jvm") 
    String span(Integer from, Integer to) {
        value len = size;
        if (len == 0) {
            return "";
        }
        value reverse = to < from;
        Integer _to;
        Integer _from;
        if (reverse) {
            _to = from;
            _from = to;
        }
        else {
            _to = to;
            _from = from;
        }
        if (_to < 0 || _from >= len) {
            return "";
        }
        value begin = _from < 0 then 0 else _from;
        value start = startIndex(begin);
        String result;
        if (_to >= len) {
            result = builder.substring(start);
        }
        else {
            value end = endIndex(start, _to+1 - begin);
            result = builder.substring(start, end);
        }
        return reverse then result.reversed else result;
    }
    
    shared actual native("jvm") 
    String spanFrom(Integer from) {
        if (from <= 0) {
            return string;
        }
        value len = size;
        if (len == 0 || from >= len) {
            return "";
        }
        return builder.substring(startIndex(from));
    }
    
    shared actual native("jvm") 
    String spanTo(Integer to) {
        value len = size;
        if (len == 0 || to < 0) {
            return "";
        }
        if (to >= len) {
            return string;
        }
        return builder.substring(0, startIndex(to+1));
    }
    
    Integer startIndex(Integer index) 
            => builder.offsetByCodePoints(0, index);
    
    Integer endIndex(Integer start, Integer length) 
            => builder.offsetByCodePoints(start, length);
    
}

shared native("js") final class StringBuilder() 
        satisfies SearchableList<Character> &
                  Ranged<Integer,Character,String> &
                  IndexedCorrespondenceMutator<Character> {
    
    shared actual native("js") variable String string = "";
        
    shared actual native("js") Integer size => string.size;
    
    shared actual native("js") Boolean empty => string.empty;
    
    shared actual native("js") Integer? lastIndex 
            => if (string.size == 0)
            then null
            else string.size - 1;
    
    shared actual native("js") 
    Iterator<Character> iterator() 
            => string.iterator();
    
    shared actual native("js")
    String measure(Integer from, Integer length)
            => string[from:length];
    
    shared actual native("js")
    String span(Integer from, Integer to) => string[from:to];
    
    shared actual native("js")
    String spanTo(Integer to) => string[...to];
    
    shared actual native("js")
    String spanFrom(Integer from) => string[from...];
    
    shared actual native("js")
    Character? getFromFirst(Integer index) 
            => string.getFromFirst(index);
    
    shared native("js") 
    StringBuilder append(String string) {
        this.string = this.string + string;
        return this;
    }
    
    shared native("js") 
    StringBuilder prepend(String string) {
        this.string = string + this.string;
        return this;
    }
    
    shared native("js") 
    StringBuilder appendCharacter(Character character) {
        string = string + character.string;
        return this;
    }
    
    shared native("js") 
    StringBuilder prependCharacter(Character character) {
        string = character.string + string;
        return this;
    }
    
    shared native("js") 
    StringBuilder clear() {
        string = "";
        return this;
    }
    
    shared native("js") 
    StringBuilder insert(Integer index, String string) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        this.string 
                = this.string[0:index] 
                + string 
                + this.string[index...];
        return this;
    }
    
    shared native("js") 
    StringBuilder insertCharacter
            (Integer index, Character character) 
            => insert(index, character.string);
    
    shared native("js") 
    StringBuilder replace
            (Integer index, Integer length, String string) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        "index+length must not be greater than size"
        assert (index+length<=size);
        value len = length<0 then 0 else length;
        this.string 
                = this.string[0:index] 
                + string + 
                this.string[index+len...];
        return this;
    }
    
    shared native("js") 
    StringBuilder delete(Integer index, Integer length) {
        "index must not be negative"
        assert (index>=0);
        "index must not be greater than size"
        assert (index<=size);
        "index+length must not be greater than size"
        assert (index+length<=size);
        if (length>0) {
            string = string[0:index] 
                   + string[index+length...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder deleteInitial(Integer length) {
        "length must not be greater than size"
        assert (length<=size);
        if (length>0) {
            string = string[length...];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder deleteTerminal(Integer length) {
        "length must not be greater than size"
        assert (length<=size);
        if (length>0) {
            string = string[0:size-length];
        }
        return this;
    }
    
    shared native("js") 
    StringBuilder reverseInPlace() {
        string = string.reversed;
        return this;
    }
    
    shared actual native("js")
    Integer? firstInclusion(List<Character> sublist,
        Integer from) 
            => string.firstInclusion(sublist, from);
    
    shared actual native("js")
    Integer? lastInclusion(List<Character> sublist,
        Integer from) 
            => string.lastInclusion(sublist, from);
    
    shared actual native("js")
    Integer? firstOccurrence(Character character,
        Integer from, Integer length) 
            => string.firstOccurrence(character, from, length);
    
    shared actual native("js")
    Integer? lastOccurrence(Character character,
        Integer from, Integer length) 
            => string.lastOccurrence(character, from, length);
    
    shared actual native("js")
    {Integer*} inclusions(List<Character> sublist, 
        Integer from)
            => string.inclusions(sublist, from);
    
    shared actual native("js")
    {Integer*} occurrences(Character character, 
        Integer from, Integer length)
            => string.occurrences(character, from, length);
    
}

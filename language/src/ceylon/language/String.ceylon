doc "A string of characters. Each character in the string is 
     a 32-bit Unicode character. The internal UTF-16 
     encoding is hidden from clients.
     
     A string is a `Category` of its `Character`s, and of
     its substrings:
     
         `w` in greeting 
         \"hello\" in greeting
     
     Strings are summable:
     
         String greeting = \"hello\" + \" \" + \"world\";
     
     They are efficiently iterable:
     
         for (char in \"hello world\") { ... }
     
     They are `List`s of `Character`s:
     
         value char = \"hello world\"[5];
     
     They are ranged:
     
         String who = \"hello world\"[6...];
     
     Note that since `string[index]` evaluates to the 
     optional type `Character?`, it is often more convenient
     to write `string[index..index]`, which evaluates to a
     `String` containing a single character, or to the empty
     string \"\" if `index` refers to a position outside the
     string.
     
     The `string()` function makes it possible to use 
     comprehensions to transform strings:
     
         string(for (s in \"hello world\") if (s.letter) s.uppercased)
     
     Since a `String` has an underlying UTF-16 encoding, 
     certain operations are expensive, requiring iteration
     of the characters of the string. In particular, `size`
     requires iteration of the whole string, and `item()`,
     `span()`, and `segment()` require iteration from the 
     beginning of the string to the given index."
by "Gavin"
see (string)
shared abstract class String()
        extends Object()
        satisfies Character[] & 
                  Comparable<String> &
                  Summable<String> & 
                  Ranged<Integer,String> &
                  Castable<String> &
                  Cloneable<String> {
    
    doc "The characters in this string."
    shared formal Character[] characters;
    
    doc "This string, with all characters in lowercase."
    shared formal String lowercased;
    
    doc "This string, with all characters in uppercase."
    shared formal String uppercased;
    
    doc "Split the string into tokens, using the given
         predicate to determine which characters are 
         separator characters."
    shared formal {String...} split(
            doc "A predicate that determines if a character
                 is a separator characters at which to split.
                 Default to split at any Unicode whitespace
                 character."
            {Character...}|Boolean(Character) separator 
                    = (Character ch) => ch.whitespace,
            doc "Specifies that the separator characters
                 occurring in the string should be discarded.
                 If `false`, they will be included in the
                 resulting iterator."
            Boolean discardSeparators=true,
            doc "Specifies that the separator tokens should 
                 be grouped eagerly and not be treated as 
                 single-character tokens. If `false` each 
                 separator token will be of size `1`."
            Boolean groupSeparators=true);
    
    doc "Join the given strings, using this string as a 
         separator."
    shared formal String join(String... strings);
    
    doc "Split the string into lines of text."
    shared formal {String...} lines;

    doc "This string, after discarding whitespace from the 
         beginning and end of the string."
    shared formal String trimmed;

    doc "This string, after collapsing strings of whitespace 
         into single space characters and discarding whitespace 
         from the beginning and end of the string."
    shared formal String normalized;
    
    doc "This string, with the characters in reverse order."
    shared formal actual String reversed;
    
    doc "Select the characters between the given indexes.
         If the start index is the same as the end index,
         return a string with a single character.
         If the start index is larger than the end index, 
         return the characters in the reverse order from
         the order in which they appear in this string.
         If both the start index and the end index are 
         larger than the last index in the string, return 
         the empty string. Otherwise, if the last index is 
         larger than the last index in the sequence, return
         all characters from the start index to last 
         character of the string."
    shared actual formal String span(Integer from, Integer to);

	shared actual String spanFrom(Integer from) =>
        span(from, size);

    shared actual String spanTo(Integer to) =>
        to>0 then span(0, to) else "";
    
    doc "Select the characters of this string beginning at 
         the given index, returning a string no longer than 
         the given length. If the portion of this string
         starting at the given index is shorter than 
         the given length, return the portion of this string
         from the given index until the end of this string. 
         Otherwise return a string of the given length. If 
         the start index is larger than the last index of the 
         string, return the empty string."
    shared formal actual String segment(Integer from, 
                                        Integer length);
    
    doc "Select the first characters of this string, 
         returning a string no longer than the given 
         length. If this string is shorter than the given
         length, return this string. Otherwise return a
         string of the given length."
    shared formal String initial(Integer length);
    
    doc "Select the last characters of the string, 
         returning a string no longer than the given 
         length. If this string is shorter than the given
         length, return this string. Otherwise return a
         string of the given length."
    shared formal String terminal(Integer length);
    
    doc "The length of the string (the number of characters
         it contains). In the case of the empty string, the
         string has length zero. Note that this operation is
         potentially costly for long strings, since the
         underlying representation of the characters uses a
         UTF-16 encoding."
    see (longerThan, shorterThan)
    shared actual formal Integer size;
    
    doc "The index of the last character in the string, or
         `null` if the string has no characters. Note that 
         this operation is potentially costly for long 
         strings, since the underlying representation of the 
         characters uses a UTF-16 encoding."
    shared actual Integer? lastIndex {
        if (size==0) {
            return null;
        }
        else {
            return size-1;
        }
    }
    
    doc "An iterator for the characters of the string."
    shared actual formal Iterator<Character> iterator;
    
    doc "Returns the character at the given index in the 
         string, or `null` if the index is past the end of
         string. The first character in the string occurs at
         index zero. The last character in the string occurs
         at index `string.size-1`."
    shared actual formal Character? item(Integer index);
    
    doc "The character indexes at which the given substring
         occurs within this string. Occurrences do not 
         overlap."
    shared formal {Integer...} occurrences(String substring);
    
    doc "The first index at which the given substring occurs
         within this string, or `null` if the substring does
         not occur in this string."
    shared formal Integer? firstOccurrence(String substring);
    
    doc "The last index at which the given substring occurs
         within this string, or `null` if the substring does
         not occur in this string."
    shared formal Integer? lastOccurrence(String substring);
    
    doc "The first index at which the given character occurs
         within this string, or `null` if the character does
         not occur in this string."
    shared formal Integer? firstCharacterOccurrence(Character substring);
    
    doc "The last index at which the given character occurs
         within this string, or `null` if the character does
         not occur in this string."
    shared formal Integer? lastCharacterOccurrence(Character substring);
    
    doc "Determines if the given object is a `String` and, 
         if so, if it occurs as a substring of this string,
         or if the object is a `Character` that occurs in
         this string. That is to say, a string is considered 
         a `Category` of its substrings and of its 
         characters."
    shared actual formal Boolean contains(Object element);
    
    doc "Determines if this string starts with the given 
         substring."
    shared formal Boolean startsWith(String substring);
    
    doc "Determines if this string ends with the given 
         substring."
    shared formal Boolean endsWith(String substring);
        
    doc "Returns the concatenation of this string with the
         given string."
    shared actual formal String plus(String other);
    
    doc "Returns a string formed by repeating this string
         the given number of times."
    shared formal String repeat(Integer times);
    
    doc "Returns a string formed by replacing every 
         occurrence in this string of the given substring
         with the given replacement string, working from 
         the start of this string to the end."
    shared formal String replace(String substring, 
                                 String replacement);
    
    doc "Compare this string with the given string 
         lexicographically, according to the Unicode values
         of the characters."
    shared actual formal Comparison compare(String other);
    
    doc "Determines if this string is longer than the given
         length. This is a more efficient operation than
         `string.size>length`."
    see (size)
    shared formal Boolean longerThan(Integer length);
    
    doc "Determines if this string is shorter than the given
         length. This is a more efficient operation than
         `string.size>length`."
    see (size)
    shared formal Boolean shorterThan(Integer length);
    
    doc "Determines if the given object is a string, and if
         so, if this string has the same length, and the 
         same characters, in the same order, as the given 
         string."
    shared actual formal Boolean equals(Object that);
    
    shared actual formal Integer hash;
    
    doc "Returns the string itself."
    shared actual String string => this;
    
    doc "Determines if this string has no characters, that
         is, if it has zero `size`. This is a more efficient 
         operation than `string.size==0`."
    see (size)
    shared actual formal Boolean empty;
    
    doc "Returns this string."
    shared actual String coalesced => this;
    
}

doc "Create a new string containing the given characters."
shared String string(Character... characters) { throw; }

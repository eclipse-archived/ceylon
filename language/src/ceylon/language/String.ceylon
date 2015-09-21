"""A string of characters. Each character in the string is a 
   [[32-bit Unicode character|Character]]. The internal 
   UTF-16 encoding is hidden from clients.
   
   Literal strings may be written between double quotes:
   
       "hello world"
       "\r\n"
       "\{#03C0} \{#2248} 3.14159"
       "\{GREEK SMALL LETTER PI} \{ALMOST EQUAL TO} 3.14159"
   
   Alternatively, a _verbatim string_ may be written between
   tripled double quotes.
   
   The _empty string_, `""`, is a string with no characters.
   
   A string is a [[Category]] of its [[characters]], and of 
   its substrings:
   
       'w' in greeting 
       "hello" in greeting
   
   Strings are [[summable|Summable]]:
   
       String greeting = "hello" + " " + "world";
   
   They are efficiently [[iterable|Iterable]]:
   
       for (char in "hello world") { ... }
   
   They are [[lists|List]] of [[characters|Character]]:
   
       value char = "hello world"[5];
   
   They are [[ranged|Ranged]]:
   
       String who = "hello world"[6...];
   
   Note that since `string[index]` evaluates to the optional 
   type `Character?`, it is often more convenient to write 
   `string[index..index]`, which evaluates to a `String` 
   containing a single character, or to the empty string 
   `""` if `index` refers to a position outside the string.
   
   It is easy to use comprehensions to transform strings:
   
       String { for (s in "hello world") if (s.letter) s.uppercased }
   
   Since a `String` has an underlying UTF-16 encoding, 
   certain operations are expensive, requiring iteration of 
   the characters of the string. In particular, [[size]]
   requires iteration of the whole string, and `get()`,
   `span()`, and `measure()` require iteration from the 
   beginning of the string to the given index."""
by ("Gavin")
tagged("Basic types")
shared native final class String(characters)
        extends Object()
        satisfies List<Character> & 
                  Comparable<String> &
                  Summable<String> & 
                  Ranged<Integer,Character,String> {
    
    "The characters that form this string."
    {Character*} characters;
    
    "This string, with all characters in lowercase.
     
     Conversion of uppercase characters to lowercase is
     performed according to a locale-independent mapping
     that produces incorrect results in certain locales
     (e.g. `tr-TR`).
     
     The resulting string may not have the same number of
     characters as this string, since the uppercase 
     representation of certain characters comprises multiple
     characters, for example the lowercase representation of 
     \{LATIN CAPITAL LETTER I WITH DOT ABOVE} is two 
     characters wide."
    shared native String lowercased;
    
    "This string, with all characters in uppercase.
     
     Conversion of lowercase characters to uppercase is
     performed according to a locale-independent mapping
     that produces incorrect results in certain locales
     (e.g. `tr-TR`).
     
     The resulting string may not have the same number of
     characters as this string, since the uppercase 
     representation of certain characters comprises multiple
     characters, for example the uppercase representation of 
     \{LATIN SMALL LETTER SHARP S} is SS."
    shared native String uppercased;
    
    "Split the string into tokens, using the given 
     [[predicate function|splitting]] to determine which 
     characters are separator characters.
     
         value pathElements = path.split('/'.equals);
     
     The flags [[discardSeparators]] and [[groupSeparators]]
     determine how separator characters should occur in the
     resulting stream.
     
     Note that for the case of the empty string, `split()` 
     always produces a stream containing a single empty 
     token. For example:
     
         \"\".split('/'.equals)
     
     evaluates to the nonempty stream `{ \"\" }`."
    shared native {String+} split(
            "A predicate that determines if a character is a
             separator characters at which to split. Default 
             to split at any 
             [[whitespace|Character.whitespace]] character."
            Boolean splitting(Character ch) => ch.whitespace,
            "Specifies that the separator characters
             occurring in the string should be discarded. If 
             `false`, they will be included in the resulting 
             iterator."
            Boolean discardSeparators=true,
            "Specifies that the separator tokens should be 
             grouped eagerly and not be treated as 
             single-character tokens. If `false` each 
             separator token will be of size `1`."
            Boolean groupSeparators=true);
    
    "The first character in the string."
    shared actual native Character? first;
    
    "The last character in the string."
    shared actual native Character? last;
    
    "The rest of the string, without its first character."
    shared actual native String rest;
    
    //"Get the character at the specified index, or `null` if
    // the index falls outside the bounds of this string."
    //shared actual native Character? get(Integer index);
    
    "Get the character at the specified index, where the 
     string is indexed from the _end_ of the string, or 
     `null` if the index falls outside the bounds of this 
     string."
    shared actual native Character? getFromLast(Integer index);
    
    "A sequence containing all indexes of this string."
    shared actual native Integer[] keys => 0:size;
    
    "Join the [[string representations|Object.string]] of 
     the given [[objects]], using this string as a separator."
    shared native String join({Object*} objects);
    
    "Split the string into lines of text, discarding line
     breaks. Recognized line break sequences are `\\n` and 
     `\\r\\n`."
    see (`value linesWithBreaks`)
    shared native {String*} lines 
            => split('\n'.equals, true, false)
               .spread(String.trimTrailing)('\r'.equals);
    
    "Split the string into lines of text with line breaks.
     Each line will be terminated by a line break sequence,
     `\\n` or `\\r\\n`."
    see (`value lines`)
    shared native {String*} linesWithBreaks
            => split('\n'.equals, false, false)
            .partition(2)
            .map((lineWithBreak)
                    => let (line = lineWithBreak[0], 
                            br = lineWithBreak[1])
                    if (exists br) then line+br else line);
    
    "A string containing the characters of this string, 
     after discarding [[whitespace|Character.whitespace]] 
     from the beginning and end of the string."
    shared native String trimmed => trim(Character.whitespace);
    
    "A string containing the characters of this string, 
     after discarding the characters matching the given 
     [[predicate function|trimming]] from the beginning and 
     end of the string.
     
         value trimmed = name.trim('_'.equals);
     
     A character is removed from the string if it matches
     the given predicate and if either:
     
     - every character occurring earlier in the string also 
       matches the predicate, or
     - every character occurring later in the string also
       matches the predicate."
    shared actual native String trim(
            "The predicate function that determines whether
             a character should be trimmed"
            Boolean trimming(Character element));
    
    "A string containing the characters of this string, 
     after discarding the characters matching the given 
     [[predicate function|trimming]] from the 
     beginning of the string.
     
     A character is removed from the string if it matches
     the given predicate and every character occurring 
     earlier in the string also matches the predicate."
    shared actual native String trimLeading(
            "The predicate function that determines whether
             a character should be trimmed"
            Boolean trimming(Character element));
    
    "A string containing the characters of this string, 
     after discarding the characters matching the given 
     [[predicate function|trimming]] from the end of the 
     string.
     
     A character is removed from the string if it matches
     the given predicate and every character occurring 
     later in the string also matches the predicate."
    shared actual native String trimTrailing(
            "The predicate function that determines whether
             a character should be trimmed"
            Boolean trimming(Character element));

    "A string containing the characters of this string after 
     collapsing strings of [[whitespace|Character.whitespace]] 
     into single space characters and discarding whitespace 
     from the beginning and end of the string."
    shared native String normalized;
    
    "A string containing the characters of this string, with
     the characters in reverse order."
    shared native actual String reversed;
    
    "Determines if this string contains a character at the
     given [[index]], that is, if `0<=index<size`."
    shared native actual Boolean defines(Integer index);
    
    "A string containing the characters of this string 
     between the given indexes. If the [[start index|from]] 
     is the same as the [[end index|to]], return a string 
     with a single character. If the start index is larger 
     than the end index, return the characters in the 
     reverse order from the order in which they appear in 
     this string. If both the start index and the end index 
     are larger than the last index in the string, or if 
     both the start index and the end index are smaller than
     the first index in the string, return the empty string. 
     Otherwise, if the last index is larger than the last 
     index in the string, return all characters from the 
     start index to last character of the string."
    shared actual native String span(Integer from, Integer to);
    
    "A string containing the characters of this string from 
     the given [[start index|from]] inclusive to the end of 
     the string. If the start index is larger than the last 
     index of the string, return the empty string. If the
     start index is negative, return this string."
    shared actual native String spanFrom(Integer from)
            => from<size then span(from, size) else "";
    
    "A string containing the characters of this string from 
     the start of the string up to and including the given 
     [[end index|to]]. If the end index is negative, return 
     the empty string. If the end index is larger than the
     last index in this string, return this string."
    shared actual native String spanTo(Integer to)
            => to>=0 then span(0, to) else "";
    
    "A string containing the characters of this string 
     beginning at the given [[start index|from]], returning 
     a string no longer than the given [[length]]. If the 
     portion of this string starting at the given index is 
     shorter than the given length, return the portion of 
     this string from the given index until the end of this 
     string. Otherwise, return a string of the given length. 
     If the start index is larger than the last index of the 
     string, return the empty string."
    shared native actual String measure(Integer from, 
                                        Integer length);
    
    "Select the first characters of this string, returning a 
     string no longer than the given [[length]]. If this 
     string is shorter than the given length, return this 
     string. Otherwise, return a string of the given length."
    shared native actual String initial(Integer length);
    
    "Select the last characters of the string, returning a 
     string no longer than the given [[length]]. If this 
     string is shorter than the given length, return this 
     string. Otherwise, return a string of the given length."
    shared native actual String terminal(Integer length);
    
    "Return two strings, the first containing the characters
     that occur before the given [[index]], the second with
     the characters that occur after the given `index`. If 
     the given `index` is outside the range of indices of 
     this string, one of the returned strings will be empty."
    shared native actual [String,String] slice(Integer index);
    
    "The length of the string (the number of characters it 
     contains). In the case of the empty string, the string 
     has length zero. Note that this operation is 
     potentially costly for long strings, since the
     underlying representation of the characters uses a
     UTF-16 encoding. Use of [[longerThan]] or 
     [[shorterThan]] is highly recommended."
    see (`function longerThan`, `function shorterThan`)
    shared actual native Integer size;
    
    "The index of the last character in the string, or 
     `null` if the string has no characters. Note that this 
     operation is potentially costly for long strings, since 
     the underlying representation of the characters uses a 
     UTF-16 encoding. For any nonempty string:
     
         string.lastIndex == string.size-1"
    shared actual Integer? lastIndex {
        if (size==0) {
            return null;
        }
        else {
            return size-1;
        }
    }
    
    "An iterator for the characters of the string."
    shared actual native Iterator<Character> iterator();
    
    "Returns the character at the given [[index]] in the 
     string, or `null` if the index is before the start of 
     the string or past the end of string. The first 
     character in the string occurs at index zero. The last 
     character in the string occurs at index 
     `string.size-1`."
    shared actual native Character? getFromFirst(Integer index);
    
    //shared actual native Boolean->Character? lookup(Integer index);
    
    "Determines if the given object is a `String` and, if 
     so, if it occurs as a substring of this string, or if 
     the object is a `Character` that occurs in this string. 
     That is to say, a string is considered a [[Category]] 
     of its substrings and of its characters."
    shared actual native Boolean contains(Object element);
    
    shared actual native Boolean startsWith(List<Anything> substring);
    
    shared actual native Boolean endsWith(List<Anything> substring);
    
    "Returns the concatenation of this string with the
     given string."
    shared actual native String plus(String other);
    
    "Returns a string formed by repeating this string the 
     given number of [[times]], or the empty string if
     `times<=0`."
    shared actual native String repeat(Integer times);
    
    "Returns a string formed by replacing every occurrence 
     in this string of the given [[substring]] with the 
     given [[replacement]] string, working from the start of
     this string to the end."
    shared native String replace(String substring, 
                                 String replacement);
    
    "Returns a string formed by replacing the first 
     occurrence in this string of the given [[substring]], 
     if any, with the given [[replacement]] string."
    shared native String replaceFirst(String substring, 
                                      String replacement);
    
    "Returns a string formed by replacing the last 
     occurrence in this string of the given [[substring]], 
     if any, with the given [[replacement]] string."
    shared native String replaceLast(String substring, 
                                     String replacement);
    
    "Compare this string with the given string 
     lexicographically, according to the Unicode code points
     of the characters.
     
     This defines a locale-independent collation that is
     incorrect in some locales."
    shared actual native Comparison compare(String other) {
        value min = smallest(size, other.size);
        for (i in 0:min) {
            assert (exists thisChar = this.getFromFirst(i),
                    exists thatChar = other.getFromFirst(i));
            if (thisChar!=thatChar) {
                return thisChar <=> thatChar;
            }
        }
        return size <=> other.size;
    }
    
    "Compare this string with the given string 
     lexicographically, ignoring the case of the characters.
     That is, by considering two characters `x` and `y` as
     equal if:
     
     - `x == y`,
     - `x.uppercased == y.uppercased`, or
     - `x.lowercased == y.lowercased`.
     
     This defines a locale-independent collation that is
     incorrect in some locales."
    see (`value Character.lowercased`, 
         `value Character.uppercased`)
    shared native Comparison compareIgnoringCase(String other) {
        value min = smallest(size, other.size);
        for (i in 0:min) {
            assert (exists thisChar = this.getFromFirst(i),
                    exists thatChar = other.getFromFirst(i));
            if (thisChar!=thatChar && 
                thisChar.uppercased!=thatChar.uppercased &&
                thisChar.lowercased!=thatChar.lowercased) {
                return thisChar.lowercased <=> thatChar.lowercased;
            }
        }
        return size <=> other.size;
    }
    
    "Determines if this string is longer than the given
     [[length]]. This is a more efficient operation than
     `string.size>length`."
    see (`value size`)
    shared actual native Boolean longerThan(Integer length);
    
    "Determines if this string is shorter than the given
     [[length]]. This is a more efficient operation than
     `string.size>length`."
    see (`value size`)
    shared actual native Boolean shorterThan(Integer length);
    
    "Determines if the given object is a `String`, and if 
     so, if this string has the same [[length|size]], and 
     the same [[characters]], in the same order, as the 
     given [[string|that]]."
    shared actual native Boolean equals(Object that) {
        if (is String that) {
            if (size!=that.size) {
                return false;
            }
            value min = smallest(size, that.size);
            for (i in 0:min) {
                assert (exists thisChar = this.getFromFirst(i),
                        exists thatChar = that.getFromFirst(i));
                if (thisChar!=thatChar) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }
    
    "Compare this string with the given string, ignoring the 
     case of the characters. That is, by considering two 
     characters `x` and `y` as equal if:
     
     - `x == y`,
     - `x.uppercased == y.uppercased`, or
     - `x.lowercased == y.lowercased`."
    see (`value Character.lowercased`, 
         `value Character.uppercased`)
    shared native Boolean equalsIgnoringCase(String that) {
        if (size!=that.size) {
            return false;
        }
        value min = smallest(size, that.size);
        for (i in 0:min) {
            assert (exists thisChar = this.getFromFirst(i),
                    exists thatChar = that.getFromFirst(i));
            if (thisChar!=thatChar && 
                thisChar.uppercased!=thatChar.uppercased &&
                thisChar.lowercased!=thatChar.lowercased) {
                return false;
            }
        }
        return true;
    }
    
    shared actual native Integer hash;
    
    "This string."
    shared actual String string => this;
    
    "Determines if this string has no characters, that is, 
     if it has zero [[size]]. This is a _much_ more 
     efficient operation than `string.size==0`."
    see (`value size`)
    shared actual native Boolean empty;
    
    "This string."
    shared actual String coalesced => this;
    
    "This string."
    shared actual String clone() => this;
    
    "Pad this string with the given [[character]], producing 
     a string of the given minimum [[size]], centering the
     string."
    shared native String pad(Integer size, 
        "The padding character"
        Character character=' ');
    
    "Left pad this string with the given [[character]], 
     producing a string of the given minimum [[size]]."
    shared native String padLeading(Integer size, 
        "The padding character"
        Character character=' ');
    
    "Right pad this string with the given [[character]], 
     producing a string of the given minimum [[size]]."
    shared native String padTrailing(Integer size, 
        "The padding character"
        Character character=' ');
    
    "Efficiently copy the characters in the segment
     `sourcePosition:length` of this string to the segment 
     `destinationPosition:length` of the given 
     [[character array|destination]]."
    shared native 
    void copyTo(
        "The array into which to copy the elements."
        Array<Character> destination,
        "The index of the first element in this array to 
         copy."
        Integer sourcePosition = 0,
        "The index in the given array into which to copy the 
         first element."
        Integer destinationPosition = 0,
        "The number of elements to copy."
        Integer length 
                = smallest(size - sourcePosition,
                    destination.size - destinationPosition));
    
    shared actual native Boolean occurs(Anything element, Integer from, Integer length);
    shared actual native Boolean occursAt(Integer index, Anything element);
    shared actual native Boolean includes(List<Anything> sublist, Integer from);
    shared actual native Boolean includesAt(Integer index, List<Anything> sublist);
        
    shared actual native Integer? firstOccurrence(Anything element, Integer from, Integer length);
    shared actual native Integer? lastOccurrence(Anything element, Integer to);
    shared actual native Integer? firstInclusion(List<Anything> sublist, Integer from);
    shared actual native Integer? lastInclusion(List<Anything> sublist, Integer to);
    
    shared actual native Integer countOccurrences(Anything sublist, Integer from, Integer length);
    shared actual native Integer countInclusions(List<Anything> sublist, Integer from);
        
    shared actual native Boolean largerThan(String other); 
    shared actual native Boolean smallerThan(String other); 
    shared actual native Boolean notSmallerThan(String other); 
    shared actual native Boolean notLargerThan(String other);
    
    shared actual native void each(void step(Character element));
    shared actual native Integer count(Boolean selecting(Character element));
    shared actual native Boolean every(Boolean selecting(Character element));
    shared actual native Boolean any(Boolean selecting(Character element));
    shared actual native Result|Character|Null reduce<Result>
            (Result accumulating(Result|Character partial, Character element));
    shared actual native Character? find(Boolean selecting(Character element));
    shared actual native Character? findLast(Boolean selecting(Character element));
    shared actual native <Integer->Character>? locate(Boolean selecting(Character element));
    shared actual native <Integer->Character>? locateLast(Boolean selecting(Character element));
}

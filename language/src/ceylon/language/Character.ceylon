"""A 32-bit [Unicode][] character.
   
   Literal characters may be written between single quotes:
   
       ' '
       '\n'
       '\{#03C0}'
       '\{GREEK SMALL LETTER PI}'
   
   Every `Character` has a unique [[Integer]]-valued Unicode 
   _code point_.
   
       Integer piCodePoint = '\{GREEK SMALL LETTER PI}'.integer; // #03C0
       Character pi = #03C0.character; // GREEK SMALL LETTER PI
   
   Characters are [[Enumerable]], so character ranges may be
   produced using the [[measure]] and [[span]] operators.
   
       value lowerLatinLetters = 'a'..'z';
       value upperLatinLetters = 'A':26;
   
   Characters have a [[natural order|Comparable]] determined
   by their Unicode code points. So, for example, `'a'<'b'`,
   since `'a'.integer<'b'.integer`.
   
   [Unicode]: http://www.unicode.org/"""
see (`class String`)
by ("Gavin")
tagged("Basic types", "Strings")
shared final native class Character(Character character)
        extends Object()
        satisfies Comparable<Character> & 
                  Enumerable<Character> {
    
    "A string containing just this character."
    shared actual native String string;
    
    "The lowercase representation of this character.
     
     Conversion of uppercase characters to lowercase is
     performed according to a locale-independent mapping
     that produces incorrect results in certain locales
     (e.g. `tr-TR`).
     
     Furthermore, this conversion always produces a single
     character, which is incorrect for characters whose
     uppercase representation comprises multiple characters,
     for example \{LATIN CAPITAL LETTER I WITH DOT ABOVE}. 
     Thus,
     
     - `'\{LATIN CAPITAL LETTER I WITH DOT ABOVE}'.uppercased`
       evaluates to `'i'`, whereas
     - `\"\{LATIN CAPITAL LETTER I WITH DOT ABOVE}\".uppercased`
       evaluates, more correctly, to the string 
       `\"i\{COMBINING DOT ABOVE}\"`.
     
     Therefore, for most purposes, it is better to use 
     `char.string.lowercased` instead of `char.lowercased`."
    see (`value String.lowercased`)
    shared native Character lowercased;
    
    "The uppercase representation of this character, in the
     [[system]] default locale.
     
     Conversion of lowercase characters to uppercase is
     performed according to a locale-independent mapping
     that produces incorrect results in certain locales
     (e.g. `tr-TR`).
     
     Furthermore, this conversion always produces a single
     character, which is incorrect for characters whose
     uppercase representation comprises multiple characters,
     for example \{LATIN SMALL LETTER SHARP S}. Thus,
     
     - `'\{LATIN SMALL LETTER SHARP S}'.uppercased`
       evaluates to `'\{LATIN SMALL LETTER SHARP S}'`, 
       whereas
     - `\"\{LATIN SMALL LETTER SHARP S}\".uppercased`
       evaluates, more correctly, to the string `\"SS\"`.
     
     Therefore, for most purposes, it is better to use 
     `char.string.uppercased` instead of `char.uppercased`."
    see (`value String.uppercased`)
    shared native Character uppercased;
    
    "The title case representation of this character."
    shared native Character titlecased;
    
    "Determine if this is a lowercase representation of the
     character. That is, if its Unicode general category is 
     *Ll*."
    shared native Boolean lowercase;
    
    "Determine if this is an uppercase representation of the
     character. That is, if its Unicode general category is 
     *Lu*."
    shared native Boolean uppercase;
    
    "Determine if this is a title case representation of the
     character. That is, if its Unicode general category is 
     *Lt*."
    shared native Boolean titlecase;

    "Determine if this character is a numeric digit. That 
     is, if its Unicode general category is *Nd*."
    shared native Boolean digit;

    "Determine if this character is a letter. That is, if 
     its Unicode general category is *Lu*, *Ll*, *Lt*, *Lm*,
     or *Lo*."
    shared native Boolean letter;

    "Determine if this character is a whitespace character. 
     The following characters are whitespace characters:
     
     - *LINE FEED*, `\\n` or `\\{#000A}`,
     - *FORM FEED*, `\\f` or `\\{#000C}`,
     - *CARRIAGE RETURN*, `\\r` or `\\{#000D}`,
     - *HORIZONTAL TABULATION*, `\\t` or `\\{#0009}`,
     - *LINE TABULATION*, `\\{#000B}`,
     - *FILE SEPARATOR*, `\\{#001C}`,
     - *GROUP SEPARATOR*, `\\{#001D}`,
     - *RECORD SEPARATOR*, `\\{#001E}`,
     - *UNIT SEPARATOR*, `\\{#001F}`, and
     - any Unicode character in the general category *Zs*, 
       *Zl*, or *Zp* that is not a non-breaking space."
    shared native Boolean whitespace;
    
    "Determine if this character is an ISO control 
     character."
    shared native Boolean control;
    
    /*"The general category of the character"
    shared native CharacterCategory category;*/
    
    /*"The directionality of the character."
    shared native CharacterDirectionality directionality;*/
    
    "The Unicode code point of the character, an [[Integer]]
     in the range `0..#10FFFF`."
    aliased("codePoint")
    shared native Integer integer;
    
    "Compare this character with the given string character, 
     according to the Unicode code points of the characters."
    shared actual native Comparison compare(Character other);
    
    "Determines if the given object is a character with the
     same code point as this character."
    shared actual native Boolean equals(Object that);
    
    "The code point of the character."
    shared actual native Integer hash;
    
    "The character with the unicode code point that is one
     greater than this character."
    shared actual native Character predecessor;

    "The character with the unicode code point that is one
     less than this character."
    shared actual native Character successor;
    
    shared actual native Character neighbour(Integer offset);
    shared actual native Integer offset(Character other);
    shared actual native Integer offsetSign(Character other);
    
    shared actual native Boolean largerThan(Character other); 
    shared actual native Boolean smallerThan(Character other); 
    shared actual native Boolean notSmallerThan(Character other); 
    shared actual native Boolean notLargerThan(Character other); 
}

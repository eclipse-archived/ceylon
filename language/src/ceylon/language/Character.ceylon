"A 32-bit Unicode character."
see (`String`)
by ("Gavin")
shared final native class Character(Character character)
        extends Object()
        satisfies Comparable<Character> & 
                  Enumerable<Character> {

    "A string containg just this character."
    shared actual native String string;

    "The lowercase representation of this character."
    shared native Character lowercased;

    "The uppercase representation of this character."
    shared native Character uppercased;

    "The title case representation of this character."
    shared native Character titlecased;

    "Determine if this is a lowercase representation of
     the character."
    shared native Boolean lowercase;
    
    "Determine if this is an uppercase representation of
     the character."
    shared native Boolean uppercase;
    
    "Determine if this is a title case representation of
     the character."
    shared native Boolean titlecase;

    "Determine if this character is a numeric digit."
    shared native Boolean digit;

    "Determine if this character is a letter."
    shared native Boolean letter;

    "Determine if this character is a whitespace 
     character. A whitespace character is:
     * U+0009 HORIZONTAL TABULATION (`\\t`),
     * U+000A LINE FEED (`\\n`),
     * U+000B VERTICAL TABULATION,
     * U+000C FORM FEED (`\\f`),
     * U+000D CARRIAGE RETURN (`\\r`),
     * U+001C FILE SEPARATOR,
     * U+001D GROUP SEPARATOR,
     * U+001E RECORD SEPARATOR,
     * U+001F UNIT SEPARATOR or
     * any Unicode character in the general category *Zs* (space separator), 
       *Zl* (line separator) or *Zp* (paragraph separator)
       that is not also a non-breaking space.
     "
    shared native Boolean whitespace;
    
    "Determine if this character is an ISO control 
     character."
    shared native Boolean control;

    /*"The general category of the character"
    shared native CharacterCategory category;*/

    /*"The directionality of the character."
    shared native CharacterDirectionality directionality;*/
    
    "The code point of the character."
    shared native Integer integer;

}

doc "A 32-bit Unicode character."
see (String)
by "Gavin"
shared abstract class Character()
        extends Object()
        satisfies Comparable<Character> & 
                  Enumerable<Character> {

    doc "A string containg just this character."
    shared actual formal String string;

    doc "The lowercase representation of this character."
    shared formal Character lowercased;

    doc "The uppercase representation of this character."
    shared formal Character uppercased;

    doc "The title case representation of this character."
    shared formal Character titlecased;

    doc "Determine if this is a lowercase representation of
         the character."
    shared formal Boolean lowercase;
    
    doc "Determine if this is an uppercase representation of
         the character."
    shared formal Boolean uppercase;
    
    doc "Determine if this is a title case representation of
         the character."
    shared formal Boolean titlecase;

    doc "Determine if this character is a numeric digit."
    shared formal Boolean digit;

    doc "Determine if this character is a letter."
    shared formal Boolean letter;

    doc "Determine if this character is a whitespace 
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
    shared formal Boolean whitespace;
    
    doc "Determine if this character is an ISO control 
         character."
    shared formal Boolean control;

    /*doc "The general category of the character"
    shared formal CharacterCategory category;*/

    /*doc "The directionality of the character."
    shared formal CharacterDirectionality directionality;*/
    
    doc "The code point of the character."
    shared formal Integer integer;

}

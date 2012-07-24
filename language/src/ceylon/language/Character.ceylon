doc "A 32-bit Unicode character."
see (String)
by "Gavin"
shared abstract class Character()
        extends Object()
        satisfies Ordinal<Character> & Comparable<Character> {

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
         character."
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

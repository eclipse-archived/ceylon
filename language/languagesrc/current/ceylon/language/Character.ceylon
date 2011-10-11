doc "A UTF-?? character."
see (String)
by "Gavin"
shared abstract class Character()
        extends Object()
        satisfies Ordinal<Character> & Comparable<Character> {

    doc "The lowercase representation of this character."
    shared formal Character lowercased;

    doc "The uppercase representation of this character."
    shared formal Character uppercased;

    doc "Determine if this is a lowercase representation of
         the character."
    shared formal Boolean lowercase;
    
    doc "Determine if this is an uppercase representation of
         the character."
    shared formal Boolean uppercase;
    
    doc "Determine if this character is a numeric digit."
    shared formal Boolean digit;

    doc "Determine if this character is a letter."
    shared formal Boolean letter;

    doc "Determine if this character is a whitespace 
         character."
    shared formal Boolean whitespace;

}
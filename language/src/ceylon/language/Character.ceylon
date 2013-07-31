"A 32-bit Unicode character."
//see (`String`)
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

    "The title case representation of this character. 
     That is, if its Unicode general category is *Lt*."
    shared native Character titlecased;

    "Determine if this is a lowercase representation of
     the character. That is, if its Unicode general 
     category is *Ll*."
    shared native Boolean lowercase;
    
    "Determine if this is an uppercase representation of
     the character. That is, if its Unicode general 
     category is *Lu*."
    shared native Boolean uppercase;
    
    "Determine if this is a title case representation of
     the character. That is, if its Unicode general 
     category is *Lt*."
    shared native Boolean titlecase;

    "Determine if this character is a numeric digit.
     That is, if its Unicode general category is *Nd*."
    shared native Boolean digit;

    "Determine if this character is a letter. That is,
     if its Unicode general category is *Lu*, *Ll*, 
     *Lt*, *Lm*, or *Lo*."
    shared native Boolean letter;

    "Determine if this character is a whitespace character. 
     The following characters are whitespace characters:
     
     * *LINE FEED*, `\\n` or `\\{#000A}`,
     * *FORM FEED*, `\\f` or `\\{#000C}`,
     * *CARRIAGE RETURN*, `\\r` or `\\{#000D}`,
     * *HORIZONTAL TABULATION*, `\\t` or `\\{#0009}`,
     * *VERTICAL TABULATION*, `\\{#000B}`,
     * *FILE SEPARATOR*, `\\{#001C}`,
     * *GROUP SEPARATOR*, `\\{#001D}`,
     * *RECORD SEPARATOR*, `\\{#001E}`,
     * *UNIT SEPARATOR*, `\\{#001F}`, and
     * any Unicode character in the general category 
       *Zs*, *Zl*, or *Zp* that is not a non-breaking 
       space."
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

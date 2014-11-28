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

public class Character(small Natural utf32)
        satisfies Ordinal, Comparable<Character>, Case<Character> { 
    
    doc "The UTF-16 encoding"
    public String utf16;
    
    doc "The UTF-8 encoding"
    public String utf8;
    
    public extension class StringToCharacter(String string) {
    
        doc "Parse the string representation of a |Character| in UTF-16"
        public extension Character parseUtf16Character() { throw }
    
        doc "Parse the string representation of a |Character| in UTF-8"
        public extension Character parseUtf8Character() { throw }
    
    }
 
}
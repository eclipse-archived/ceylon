public class String(Character... characters) 
        satisfies Comparable<String>, Sequence<Character>, Case<String> {
   
    doc "Split the string into tokens, using the given
         separator characters."
    public Iterable<String> tokens(Iterable<Character> separators=" ,;\n\l\r\t") { throw }
    
    doc "The string, with all characters in lowercase."
    public String lowercase { throw }
    
    doc "The string, with all characters in uppercase."
    public String uppercase { throw }
    
    doc "Remove the given characters from the beginning 
         and end of the string.
    public String strip(Sequence<Character> whitespace = " \n\l\r\t") { throw }
    
    doc "Collapse substrings of the given characters into
         single space characters."
    public String normalize(Sequence<Character> whitespace = " \n\l\r\t") { throw }
    
    doc "Join the given strings, using this string as
         a separator."
    public String join(String... strings) { throw }
    
}
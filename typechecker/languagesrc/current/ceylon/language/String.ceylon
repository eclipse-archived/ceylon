shared class String(Sequence<Character> chars)
        extends Object()
        satisfies Sequence<Character> & Comparable<String> {

    shared Sequence<Character> characters;
    if (is String chars) {
        characters = chars;
    }
    else {
        characters = chars.clone;
    }

    doc "Split the string into tokens, using the given
         separator characters."
    shared Iterable<String> tokens(Iterable<Character> separators=" ,;\n\f\r\t") { throw; }

    doc "Split the string into lines of text."
    shared Iterable<String> lines() { return tokens("\n\f\r"); }

    doc "The string, with all characters in lowercase."
    shared String lowercase { throw; }

    doc "The string, with all characters in uppercase."
    shared String uppercase { throw; }

    doc "Remove the given characters from the beginning
         and end of the string."
    shared String strip(Character[] whitespace = " \n\f\r\t") { throw; }

    doc "Collapse substrings of the given characters into
         single space characters."
    shared String normalize(Character[] whitespace = " \n\f\r\t") { throw; }

    doc "Join the given strings, using this string as
         a separator."
    shared String join(String... strings) { throw; }

}
shared extension class String(Character[] this)
        extends Object()
        satisfies Character[] & Comparable<String> & Matcher<String> & Format {

    Character[] chars;
    if (is String this) {
        chars = this;
    }
    else {
        chars = copy(this);
    }

    ...

    doc "Split the string into tokens, using the given
         separator characters."
    shared Iterable<String> tokens(Iterable<Character> separators=" ,;\n\l\r\t") { return ... }

    doc "Split the string into lines of text."
    shared Iterable<String> lines() { return tokens("\n\l\r") }

    shared String replace(Character with(Character character)) { return ... }

    shared String replace(Character character -> Character replacement) {
        return replace() with (Character c)
            (when (c==character) then (replacement) otherwise (c));
    }

    shared String replace(Correspondence<Character,Character> replacements) {
        return replace() with (Character c) (replacements[c] ? c);
    }

    doc "The string, with all characters in lowercase."
    shared String lowercase {
        return replace() with (Character c) (c.lowercase);
    }

    doc "The string, with all characters in uppercase."
    shared String uppercase {
        return replace() with (Character c) (c.uppercase);
    }

    doc "Remove the given characters from the beginning
         and end of the string.
    shared String strip(Character[] whitespace = " \n\l\r\t") { return ... }

    doc "Collapse substrings of the given characters into
         single space characters."
    shared String normalize(Character[] whitespace = " \n\l\r\t") { return ... }

    doc "Join the given strings, using this string as
         a separator."
    shared String join(String... strings) { return ... }

}
shared class Character(small Natural utf16)
        extends Object()
        satisfies Ordinal & Comparable<Character> & Matcher<Character> {
    //TODO finish

    doc "The UTF-8 encoding"
    shared String utf8 { return ... }

    shared Character lowercase { return .. }
    shared Character uppercase { return .. }

    shared extension class StringToCharacter(String this) {

        doc "Parse the string representation of a |Character| in UTF-16"
        shared Character parseUtf16Character() { return ... }

        doc "Parse the string representation of a |Character| in UTF-8"
        shared Character parseUtf8Character() { return ... }

    }

}
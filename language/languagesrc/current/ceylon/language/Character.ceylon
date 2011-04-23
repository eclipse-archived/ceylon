shared class Character(small Natural utf16)
        extends Object()
        satisfies Ordinal<Character> & Comparable<Character> {

    shared Character lowercase { throw; }
    shared Character uppercase { throw; }

}
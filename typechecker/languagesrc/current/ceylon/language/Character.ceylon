shared class Character(small Natural utf16)
        extends Object()
        satisfies Ordinal & Comparable<Character> {

    shared Character lowercase { throw; }
    shared Character uppercase { throw; }

}
shared abstract class Character()
        extends Object()
        satisfies Ordinal<Character> & Comparable<Character> {

    shared formal Character lowercase;
    shared formal Character uppercase;

}
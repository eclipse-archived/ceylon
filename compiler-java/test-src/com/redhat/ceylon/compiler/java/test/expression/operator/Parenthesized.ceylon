@nomodel
shared class Parenthesized() {
    Integer m() {
        Float f = (2.0E300 + (1.0 - 1.0)) / 3;
        Integer f2 = 2**(m()+1);
        return 1_000_000/(m()+1);
    }
}


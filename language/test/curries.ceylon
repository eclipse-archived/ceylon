Integer potencia(Integer base, Integer expo) => base**expo;
Integer veces(Integer x, Integer y) => x*y;

void testCurries() {
    value p2 = curry(potencia)(2);
    check(p2(0)==1, "p2 [1]");
    check(p2(8)==256, "p2 [2]");
    value triple = compose(curry(veces)(3), p2);
    check(triple(8)==768, "p2 [3]");
    //TODO: flatten, ,unflatten, shuffle
}

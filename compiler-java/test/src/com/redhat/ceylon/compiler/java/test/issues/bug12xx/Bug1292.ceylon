@noanno
class Bug1292() {
    equals(Object that) => super.equals(that);
    shared void m() {
        variable Integer i = 0;
        i += super.hash;
    }
}
@noanno
interface Bug1292I {
    shared actual default Boolean equals(Object other) => nothing;
}
@noanno
class Bug1292_2() extends Basic() satisfies Bug1292I {
    shared actual Boolean equals(Object that) => (super of Identifiable).equals(that);
}
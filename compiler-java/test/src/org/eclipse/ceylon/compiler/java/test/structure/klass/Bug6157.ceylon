@noanno
interface Bug6157Int<Foo> {
    shared class C() {
        shared class CC() {
        }
    }
    shared interface I {
        shared class IC() {
        }
    }
}
@noanno
class Bug6157Cls() satisfies Bug6157Int<String> {
}
@noanno
void bug6157Run(
    Bug6157Cls.C c, 
    Bug6157Cls.C.CC cc,
    Bug6157Cls.I i,
    Bug6157Cls.I.IC ic
) {
    //bug6157Run(Bug6157Cls().Inner());
}
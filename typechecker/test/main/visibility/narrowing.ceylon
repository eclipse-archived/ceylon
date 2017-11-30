shared class O() {
    shared abstract class C() of CC {}
    class CC() extends C() {}
}

shared void testNotVisibleNarrowing(O.C? c) {
    if (is Null c) {
    } else {
        // error: type of declaration 'c' is not visible everywhere declaration is visible: 'O.CC' involves an unshared type declaration
    }
    switch (c)
    case (is Null) {
    } else {
        // error: type of declaration 'c' is not visible everywhere declaration is visible: 'O.CC' involves an unshared type declaration
    }
}
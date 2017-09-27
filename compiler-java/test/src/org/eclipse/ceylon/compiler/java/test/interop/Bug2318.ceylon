import java.lang {
    StringBuffer
}

interface CeylonMixin satisfies Bug2318Java {
    shared default actual void apply(StringBuffer buf) { }
    shared actual void apply(Character ch, Integer i1) { }
}

class Cls() satisfies CeylonMixin {
}

class Cls2() satisfies CeylonMixin {
    shared default actual void apply(StringBuffer buf) { }
}

class Cls3() extends Cls2() {
    void m() {
        (this of CeylonMixin).apply(' ', 1);
        (this of Bug2318Java).apply(' ', 1);
    }
}
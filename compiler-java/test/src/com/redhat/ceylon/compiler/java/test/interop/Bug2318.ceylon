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

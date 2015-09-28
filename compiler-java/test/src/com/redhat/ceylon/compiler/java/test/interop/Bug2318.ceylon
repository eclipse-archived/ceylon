import java.lang {
    StringBuffer
}

interface CeylonMixin satisfies Bug2318Java {
    shared actual void apply(StringBuffer buf) { }
    shared actual void apply(Character ch, Integer i1) { }
}

class Cls() satisfies CeylonMixin {
}

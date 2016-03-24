interface Bug2048List<out Element> {}
interface Bug2048Mutator<in Element> satisfies Bug2048List<Anything>{}
interface Bug2048Mutable<Element> satisfies Bug2048Mutator<Element>&Bug2048List<Element> {}
interface Bug2048UseTop {
    shared formal Bug2048List<String> m();
}
//class Bug2048UseSub() satisfies Bug2048UseTop {
//    shared actual Bug2048Mutable<String> m() => nothing;
//}
interface Bug2048UseSub2 satisfies Bug2048UseTop {
    shared actual formal Bug2048Mutable<String> m();
}
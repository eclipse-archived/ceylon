abstract class Keys1()
        satisfies Cat {}

class Keys2()
        satisfies Cat {
    shared actual Boolean contains(@error Object element) => true;
}

class Keys3()
        satisfies Cat {
    shared actual Boolean contains(String element) => true;
}


void defaultTypeArgsRefinement() {
    Boolean(String)(Keys1) fun = Keys1.contains;
    Keys3().contains("hello");
    @error Keys3().contains(1);
}

shared interface Cat<in Element=String>
        given Element satisfies Object {
    shared formal Boolean contains(Element element);
}

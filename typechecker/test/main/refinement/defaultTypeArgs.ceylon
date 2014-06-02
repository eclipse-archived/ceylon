
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

alias F => MyFoo;
alias FB => MyFoo.Bar;

void testF() {
    F.Bar bar = MyFoo("hello").Bar();
    @error F.Bar bar0 = MyFoo(1).Bar();
    MyFoo<String>.Bar barbar = bar;
    @error MyFoo<Integer>.Bar barbar0 = bar;
    FB fb = MyFoo("hello").Bar();
    @error FB fb0 = MyFoo(1).Bar();
    MyFoo<String>.Bar barfb = fb;
    @error MyFoo<Integer>.Bar barfb0 = fb;
}

class MyFoo<T=String>(T t) {
    shared class Bar() {}
}


class CC() satisfies II&JJ {
    shared actual class Inner()
            extends super.Inner() {
        shared actual Nothing get() => nothing;
    }
}

interface JJ satisfies II {}

interface KK satisfies II<Integer> {
    shared actual class Inner()
             extends super.Inner() {
        shared actual Integer get() => 1;
    }
}

interface II<out T=String> {
    shared formal class Inner() {
        shared formal T get();
    }
}

shared interface TraitB<@error Representation=Integer> 
        given Representation satisfies Float {
    shared formal Representation modfiyB();
}

shared interface TraitA<@error Representation = TraitA> 
        given Representation satisfies TraitA<Representation> {
    shared formal Representation modfiyA();
}

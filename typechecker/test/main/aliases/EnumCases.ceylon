interface Enumer<out T> of EnumerA<T> | EnumerB<T> {
    shared formal Enumer<T> self;
}
class EnumerA<out T>() satisfies Enumer<T> {
    shared actual EnumerA<T> self => this;
}
class EnumerB<out T>() satisfies Enumer<T> {
    shared actual EnumerB<T> self => this;
}

alias EnumerCases<T> => EnumerA<T> | EnumerB<T>;

void testswitchalias<T>(Enumer<T> enumer) {
    switch (e = enumer of EnumerCases<T>)
    case (is EnumerA<Anything>) {
        value a = e; // Inferred type EnumerCases<T>&EnumerA<Anything>
        EnumerA<T> b = e; // ok
        EnumerA<T> c = b.self; // ok
        EnumerA<T> d = e.self; // EnumerA<Anything> is not assignable to EnumerA<T>
    }
    else {
        value a = e; // EnumerB<T> (good)
        EnumerB<T> b = e; // ok
        EnumerB<T> c = b.self; // ok
        EnumerB<T> d = e.self; // ok
    }
}

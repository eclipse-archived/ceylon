interface Invariant<T> {}

void quantificationOverInv() {
    alias A<T> => Invariant<T>;
    A<String> x1 = nothing;
    A<out String> x2 = nothing;
    A<in String> x3 = nothing;
    Invariant<String> y1 = x1;
    @error Invariant<String> y2 = x2;
    @error Invariant<String> y3 = x3;
}

interface Covariant<out T> {}

void quantificationOverCo() {
    alias A<T> => Covariant<T>;
    A<String> x1 = nothing;
    A<out String> x2 = nothing;
    A<in String> x3 = nothing;
    Covariant<String> y1 = x1;
    Covariant<String> y2 = x2;
    @error Covariant<String> y3 = x3;
}

interface Contravariant<in T> {}

void quantificationOverContra() {
    alias A<T> => Contravariant<T>;
    A<String> x1 = nothing;
    A<out String> x2 = nothing;
    A<in String> x3 = nothing;
    Contravariant<String> y1 = x1;
    @error Contravariant<String> y2 = x2;
    Contravariant<String> y3 = x3;
}

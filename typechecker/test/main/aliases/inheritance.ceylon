void inheritance() {
    interface Bar{}
    interface Bat{}
    interface Baz{}
    alias Alias => Bat&Baz;
    $error interface Foo satisfies Bar&Alias {}
}
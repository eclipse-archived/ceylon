@noanno
class StaticAlias {
    shared static alias Foo => String|Integer;
    shared static class Bar() { 
    }
    shared static class Baz() => Bar();
    shared new () {}
}
@noanno
void staticAlias() {
    StaticAlias.Foo f = "";
    StaticAlias.Bar bar = StaticAlias.Baz();
    StaticAlias.Baz baz = StaticAlias.Bar();
}
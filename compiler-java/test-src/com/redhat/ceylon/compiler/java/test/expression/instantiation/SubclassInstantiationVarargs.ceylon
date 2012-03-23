@nomodel
class Foo(String... name) {
}
@nomodel
class Bar(String name1, String name2) extends Foo(name1, name2) {}

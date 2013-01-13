class Child() {
    shared late Parent parent;
    @error print(parent);
}
class Parent(children) {
    shared Child* children;
}
void create() {
    Child c = Child();
    Child d = Child();
    Parent p = Parent(c, d);
    c.parent = p;
    d.parent = p;
}
late String version;
void initVersion() {
    version = "1.0";
}
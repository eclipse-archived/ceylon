late String version;
void initVersion() {
    version = "1.0";
}

interface Late1 {

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

}

interface Late2 {

class Child() {
    shared late Parent parent;
    @error print(parent);
}
class Parent(children) {
    shared Child* children;
    for (child in children) {
        child.parent = this;
    }
}
void create() {
    Child c = Child();
    Child d = Child();
    Parent p = Parent(c, d);
}

}
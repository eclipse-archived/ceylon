late String version;
void initVersion() {
    version = "1.0";
}

interface Late1 {

class Child() {
    shared late Parent parent;
    //$error 
    print(parent);
    shared Parent getParent() => parent;
    shared void setParent(Parent p) => parent = p;
}
class Parent(children) {
    shared Child* children;
}
void create() {
    Child c = Child();
    Child d = Child();
    Parent p = Parent(c, d);
    c.parent = p;
    d.setParent(p);
}

}

interface Late2 {

class Child() {
    shared late Parent parent;
    //$error 
    print(parent);
    shared Parent getParent() {
        return parent;
    }
    shared void setParent(Parent p) {
        parent = p;
    }
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

class XxX(x) {
    $error shared late String x;
}
class YyY($error shared late String y) {}
class ZzZ() {
    shared late String z = "";
}
void xXx($error late String y) {
    $error late String x;
}

class LateMutation() {
    shared late Integer x = 10;
    shared void init() {
        $error x++;
        $error x+=1;
    }
}

class LateMembers() {
    late value s0 = "";
    $error late value s1 => "";
    $error late value s2 { return ""; }
}

class LateVariance<out T>() {
    late T t1;
    $error shared late T t2;
}

abstract class WithFormalAndDefaultLate() {
    $error shared formal late String name;
    $error shared default late Integer count;
}

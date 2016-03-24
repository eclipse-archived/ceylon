class Point {
    shared Float x;
    shared Float y;
    shared new (Float xx, Float yy) {
        x = xx;
        y = yy;
    }
    shared sealed new diagonal(Float d) {
        x = d;
        y = d;
    }
}

class E() extends Point(1.0, 1.0) {}

class F extends Point {
    shared new f() extends Point(1.0, 1.0) {}
}

class A extends Point {
    shared new create() extends diagonal(0.0) {}
}

class B extends Point {
    shared new point(Float x) extends Point(x,0.0) {}
}

class Bx extends Point {
    shared new point(Float x) extends super.diagonal(x*2.0) {}
}

class C0  {
    @error shared new c0() extends Point(1.0, 1.0) {}
}

class C1 extends A {
    @error shared new create() extends Point(1.0,0.0) {}
}
@error class C2 extends create {
    @error shared new create() extends create() {}
}
class C3 extends A {
    @error shared new create() extends create() {}
}
class C4 extends A {
    shared new create() extends super.create() {}
}
class C5 extends A {
    @error shared new create() extends super.Point(1.0,2.0) {}
}
class C6 extends B {
    shared new create() extends super.point(1.0) {}
}
class C7 extends B {
    shared new create() extends point(1.0) {}
}

class Oops1 extends Point {
    shared new create() extends Point.diagonal(0.0) {}
}

class Oops2 extends E {
    @error shared new create() extends Point.diagonal(0.0) {}
}


class Up0 {
    shared new () {}
}
class Down0 extends Up0 {
    shared new () extends Up0() {}
}

class Up1 {
    shared new () {}
}
class Down1 extends Up1 {
    shared new () extends Up1() {}
}
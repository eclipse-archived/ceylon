class Point {
    shared Float x;
    shared Float y;
    shared new (Float xx, Float yy) {
        x = xx;
        y = yy;
    }
    shared sealed new Diagonal(Float d) {
        x = d;
        y = d;
    }
}

class E() extends Point(1.0, 1.0) {}

class F extends Point {
    shared new F() extends Point(1.0, 1.0) {}
}

class A extends Point {
    shared new New() extends Diagonal(0.0) {}
}

class B extends Point {
    shared new Pt(Float x) extends Point(x,0.0) {}
}

class Bx extends Point {
    shared new Pt(Float x) extends super.Diagonal(x*2.0) {}
}

class C0  {
    @error shared new C0() extends Point(1.0, 1.0) {}
}

class C1 extends A {
    @error new New() extends Point(1.0,0.0) {}
}
@error class C2 extends New {
    @error new New() extends New() {}
}
class C3 extends A {
    @error new New() extends New() {}
}
class C4 extends A {
    new New() extends super.New() {}
}
class C5 extends A {
    @error new New() extends super.Point(1.0,2.0) {}
}
class C6 extends B {
    new New() extends super.Pt(1.0) {}
}
class C7 extends B {
    new New() extends Pt(1.0) {}
}

class Oops1 extends Point {
    new New() extends Point.Diagonal(0.0) {}
}

class Oops2 extends E {
    @error new New() extends Point.Diagonal(0.0) {}
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
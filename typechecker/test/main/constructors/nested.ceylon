class Outer() {
    shared class Inner {
        shared new Inner() {}
        shared new New() {}
        shared class Deeper {
            shared new New() {}
        }
    }
}

abstract class Container() {
    @error shared formal class Inner1 {
        shared new New() {}
    }
    @error shared default class Inner2 {
        shared new New() {}
    }
}

void createOuterInner() {
    Outer.Inner inner1 = Outer().Inner.New();
    Outer.Inner.Deeper deeper1 = Outer().Inner().Deeper.New();
    @error Outer.Inner inner2 = Outer.Inner.New();
    @error Outer.Inner.Deeper deeper2 = Outer.Inner().Deeper.New();
    Outer.Inner.Deeper deeper3 = Outer().Inner.New().Deeper.New();
    @error Outer.Inner.Deeper deeper4 = Outer.Inner.New().Deeper.New();
    @error Outer.Inner.Deeper deeper5 = Outer.Inner.Deeper.New();
    @error Outer.Inner.Deeper deeper6 = Outer().Inner.New.Deeper.New();
    //value it = `new Outer.Inner.New`;
}
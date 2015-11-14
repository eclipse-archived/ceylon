import check { check }

class Foo135() {}
class Bar135() extends Foo135() {}

class Super135() {
    shared default class Inner135() => Foo135();
}

class Sub135_1() extends Super135() {
    shared actual class Inner135() => Bar135();
}
class Sub135_2() extends Super135() {
    shared actual class Inner135() => Foo135();
}
class Sub135_3() extends Super135() {
    shared actual class Inner135() extends super.Inner135() {}
}
class Sub135_4() extends Super135() {
    shared actual class Inner135() extends Foo135() {}
}
class Sub135_5() extends Super135() {
    shared actual class Inner135() extends Bar135() {}
}
class NewAlias1(x) {
    shared Integer x;
}
//unsupported: class NewAlias2() => NewAlias1(5);

void bug135() {
    Sub135_1().Inner135();
    Sub135_2().Inner135();
    Sub135_3().Inner135();
    Sub135_4().Inner135();
    Sub135_5().Inner135();
    //check(NewAlias2().x == 5, "New type alias syntax");
}

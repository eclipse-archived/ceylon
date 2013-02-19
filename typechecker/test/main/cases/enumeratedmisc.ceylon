interface Association 
    of OneToOne | OneToMany { }
@error interface OneTo satisfies Association {}
class OneToOne() satisfies OneTo {}
class OneToMany() satisfies OneTo {}
@error class Broken() satisfies Association {}


interface Interface of Class1 | Class2 {}
class Class1() satisfies Interface {}
class Class2() satisfies Interface & Sized {
    shared actual Integer size = 1;
    shared actual Boolean empty = false;
}
@error class ClassX() satisfies Interface {}


@error abstract class XX() of ww | xx | YY | ZZ {}
class ZZ() {}
class YY() extends XX() {}
class YY1() extends YY() {}
object xx extends XX() {}
object ww {}


abstract class XXX() of www | xxx | YYY | ZZZ {}
class ZZZ() extends XXX() {}
class YYY() extends XXX() {}
class YYY1() extends YYY() {}
object xxx extends XXX() {}
@error abstract class WWW() extends XXX() {}
object www extends WWW() {}

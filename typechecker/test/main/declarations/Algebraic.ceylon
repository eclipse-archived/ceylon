abstract class Algebraic() of concrete | Concrete {}
class Concrete() extends Algebraic() {}
object concrete extends Algebraic() {}

void algebraic(Algebraic z) {
    switch (z)
    case (concrete) {}
    case (is Concrete) {}
}


void enumConstraint<T>(T t) 
        given T of String | Integer {
    switch (t)
    case (is String) {
        print(t);
    }
    case (is Integer) {
        print((t/100.0).string);
    }
}

class EnumConstraint<T>(T... ti) given T of Float|Integer {
    value ts = ti.sequence;
    shared actual String string {
        switch (ts)
        case(is Empty) { print(ts); return "empty"; }
        case(is Sequence<T>) { print(ts); return "sequence";}
    }
}

void testEnumConstraints() {
    enumConstraint(20);
    enumConstraint("hello");
    @error enumConstraint(1.0);
    EnumConstraint<Integer> emp = EnumConstraint<Integer>();
    EnumConstraint<Float> foo = EnumConstraint(1.0, 2.0);
    @error EnumConstraint("hello", "world");
}

interface I {}
interface J {}

void ij<T>(T k) given T of I|J {
    @error switch (k)
    case (is I) {}
    case (is J) {}
    @error switch (k)
    case (is I) {}
    switch (k)
    case (is I) {}
    else {}
}

void testij() {
    object i satisfies I {}
    object j satisfies J {}
    object k satisfies I&J {}
    ij(i);
    ij(j);
    ij(k);
    @error ij("hello");
    @error ij(1);
}

abstract class XXX<out T>() of YYY<T> | ZZZ<T> | WWW {}

class YYY<out T>() extends XXX<T>() {}
class ZZZ<out T>() extends XXX<T>() {}
class WWW() extends XXX<Bottom>() {}

object yyy extends YYY<String>() {}

void switchit(XXX<String> x) {
    switch (x) 
    case (is YYY<String>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
    case (is WWW) {
        print("www");
    }

    switch (x) 
    case (is YYY<String>) { 
        print("yyy"); 
    }
    else {}

    @error switch (x) 
    case (is YYY<String>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
    
    @error switch (x) 
    case (is YYY<Object>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
    case (is WWW) {
        print("www");
    }

    @error switch (x) 
    case (is YYY<Bottom>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
    case (is WWW) {
        print("www");
    }

    @error switch (x) 
    case (is YYY<Integer>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
    case (is WWW) {
        print("www");
    }
}

Integer fib(Integer n) {
    switch (n<=>0)
    case (equal) {
        return 1;
    }
    case (larger) {
        return n*fib(n-1);
    }
    case (smaller) {
        throw;
    }
}

    interface Association 
        of OneToOne | OneToMany { }
    interface OneTo satisfies Association {}
    class OneToOne() satisfies OneTo {}
    class OneToMany() satisfies OneTo {}
    @error class Broken() satisfies Association {}
    
    
interface Anything of SomethingUsual | SomethingElse {}
interface SomethingUsual satisfies Anything {
    shared formal String something;
}
interface SomethingElse satisfies Anything {
    shared formal Object somethingElse;
}

void switchAnything(Anything any) {
    switch (any)
    case (is SomethingUsual) { 
        print("something");
        print(any.something); 
    }
    case (is SomethingElse) { 
        print("something else"); 
        print(any.somethingElse); 
    }
}

interface Interface of Class1 | Class2 {}
abstract class Class1() of Class3 | object1 satisfies Interface {}
class Class2() satisfies Interface & Sized {
    shared actual Integer size = 1;
    shared actual Boolean empty = false;
}
class Class3() extends Class1() {}
object object1 extends Class1() {}

void switchInterface(Interface i) {
    
    if (is Sized i) {
        @type["Interface&Sized"] value ii = i;
        print(i.size);
        switch(i)
        case (is Class1&Sized) {
            print(i.size);
        }
        case (is Class2) {
            print(i.size);
        }
    }
    
    switch(i)
    case (is Class1) {}
    case (is Class2) {}
        
    switch(i)
    case (is Class1) {}
    else {}
        
    //@error 
    switch(i)
    case (is Class1) {}
    case (is Class2) {}
    else {}
        
    @error switch(i)
    case (is Class1) {}
        
    //@error 
    switch(i)
    case (is String) {}
    case (is Class1) {}
    case (is Class2) {}
        
    switch(i)
    case (is Class3) {}
    case (object1) {}
    case (is Class2) {}
    
    Integer|Float num = 1;
    
    switch (num)
    case (is Integer) {}
    case (is Float) {}
    
    @error switch (num)
    case (is Integer) {}
    case (is Float) {}
    case (is String) {}
        
}

void switchUnion1(Class2|Class3|String val) {
    switch (val)
    case (is Class2) {}
    case (is Class3) {}
    case (is String) {}
    switch (val)
    case (is Class2) {}
    else {}
    @error switch (val)
    case (is Class2) {}
    case (is Class3) {}
    @error switch (val)
    case (is Class2) {}
    case (is Class3) {}
    case (is String) {}
    case (is Integer) {}
}

void switchUnion2(Class1|String val) {
    switch (val)
    case (is Class3) {}
    case (object1) {}
    case (is String) {}
    @error switch (val)
    case (is Class1) {}
    case (object1) {}
    case (is String) {}
    @error switch (val)
    case (is Class1) {}
    case (is Class3) {}
    case (is String) {}
}

@error class NonAbstract() of SubtypeOfNonAbstract {}
class SubtypeOfNonAbstract() extends NonAbstract() {}

@error abstract class Abstract() of NonSubtypeOfAbstract {}
class NonSubtypeOfAbstract() {}
 
/*interface Indirect of Abstract1 | Abstract2 {}
abstract class Abstract1() {}
abstract class Abstract2() {}
class Concrete1() extends Abstract1() satisfies Indirect {}
class Concrete2() satisfies Indirect {}
class Concrete3() extends Abstract1() {}*/

interface J1 of J2|J3 {}
interface J2 satisfies J1 {}
interface J3 satisfies J1 {}
interface J4 satisfies J1 {}

void testHardCase(J4 i) {
    
    switch(i)
    case (is J2) {
        @type["J4&J2"] value ii = i;
    }
    case (is J3) {}
    
    @error switch(i)
    case (is J2) {}
    case (is J3) {}
    case (is Nothing) {}

    @error switch(i)
    case (is J2) {}
    case (is J3) {}
    case (null) {}

    J4? mi = null;
    
    @error switch(mi)
    case (is J2) {}
    case (is J3) {}
    
    switch(mi)
    case (is J2) {}
    case (is J3) {}
    case (null) {}

    switch(mi)
    case (is J2) {
        @type["J4&J2"] value ii = mi;
    }
    case (is J3) {}
    case (is Nothing) {}
}

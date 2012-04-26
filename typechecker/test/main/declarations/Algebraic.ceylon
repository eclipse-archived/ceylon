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

class EnumConstraint<T>(T... ts) given T of Float|Integer {
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

abstract class XXX<out T>() of YYY<T> | ZZZ<T> {}

class YYY<out T>() extends XXX<T>() {}
class ZZZ<out T>() extends XXX<T>() {}

object yyy extends YYY<String>() {}

void switchit(XXX<String> x) {
    switch (x) 
    case (is YYY<String>) { 
        print("yyy"); 
    }
    case (is ZZZ<String>) { 
        print("zzz"); 
    }
}

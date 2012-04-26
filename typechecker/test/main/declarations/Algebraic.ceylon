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
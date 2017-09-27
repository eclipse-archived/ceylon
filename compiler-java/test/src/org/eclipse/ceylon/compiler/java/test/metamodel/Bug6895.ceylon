import ceylon.language.meta.model {...}

class X(String s) {}

shared void bug6895() {
    value y = `class X`.defaultConstructor;
    try {
        CallableConstructor<X,[Integer]> newx = y.apply<X, [Integer]>(); // should fail but doesn't
        throw;
    } catch (IncompatibleTypeException e) {
        assert("Incompatible type: actual type of applied declaration is CallableConstructor<X,[String]> is not compatible with expected type: CallableConstructor<X,[Integer]>. Try passing the type argument explicitly with: getDeclaredConstructor<X,[String]>()" == e.message);
    }
}
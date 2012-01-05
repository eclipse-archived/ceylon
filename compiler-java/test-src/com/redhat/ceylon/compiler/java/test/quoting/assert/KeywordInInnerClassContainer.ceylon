@nomodel
object transient {

    shared class InnerClass_0() {
    }

    shared Integer assert {
        class InnerClass_1() {
        }
        InnerClass_1 x = InnerClass_1();
        return 0;
    }

    shared void strictfp() {
        class InnerClass_2() {
        }
        InnerClass_2 x = InnerClass_2();
    }
}

@nomodel
void m() {
    transient.InnerClass_0();
    Integer int = transient.assert;
    transient.strictfp();
}
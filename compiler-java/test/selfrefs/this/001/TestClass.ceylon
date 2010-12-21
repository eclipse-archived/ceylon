import com.redhat.ceylon.compiler.test.dump;

doc "TestClass \"this\""
class TestClass(Integer x, String y) {
    void test1() {
        dump(this);
    }

    TestClass test2() {
        return this;
    }

    void test3() {
        this.test1();
    }
}

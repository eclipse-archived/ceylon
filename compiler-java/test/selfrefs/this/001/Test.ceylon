import com.redhat.ceylon.compiler.test.dump;

doc "Test \"this\""
class Test(Integer x, String y) {
    void test1() {
        dump(this);
    }

    Test test2() {
        return this;
    }

    void test3() {
        this.test1();
    }
}

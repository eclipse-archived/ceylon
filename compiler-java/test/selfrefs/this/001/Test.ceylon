import com.redhat.ceylon.compiler.test.dump;

doc "Test \"this\""
class Test(Integer a, String b) {
    Integer x = a;
    String y = b;

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

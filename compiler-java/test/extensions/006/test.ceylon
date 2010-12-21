import com.redhat.ceylon.compiler.test.dump;

doc "Test extension to an attribute"
void test(Process process) {
    Float f = TestClass(23) + 3.142;
    dump(f);
}

import com.redhat.ceylon.compiler.test.dump;

doc "Test extension of constructor arguments"
void test(Process process) {
    TestClass t = TestClass(23);
    dump(t.value);
}

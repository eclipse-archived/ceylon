import com.redhat.ceylon.compiler.test.dump;

doc "Test extension of constructor arguments"
void test(Process process) {
    Test t = Test(23);
    dump(t.value);
}

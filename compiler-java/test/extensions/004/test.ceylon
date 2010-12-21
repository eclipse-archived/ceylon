import com.redhat.ceylon.compiler.test.dump;

doc "Test multi-step extension"
void test(Process process) {
    Float f = 3.142 + TestClass();
    dump(f);
}

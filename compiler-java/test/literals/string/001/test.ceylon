import com.redhat.ceylon.compiler.test.dump;

doc "Test string literal functionality"
void test(Process process) {
    dump("Hello world");
    dump("\n");
    dump("Hello\tworld");
}

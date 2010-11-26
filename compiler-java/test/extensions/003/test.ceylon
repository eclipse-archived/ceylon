import com.redhat.ceylon.compiler.test.dump;

doc "Test widening of the left hand side of an expression"
void test(Process process) {
    Float f = 1 + 1.5;
    dump(f);
}

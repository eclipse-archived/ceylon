import com.redhat.ceylon.compiler.test.dump;

doc "Test widening of the right hand side of an expression"
void test(Process process) {
    Float f = 1.5 + 1;
    dump(f);
}

import com.redhat.ceylon.compiler.test.dump;

doc "Test widening from mutable"
void test(Process process) {
    mutable Natural n := 17;
    Integer i = n;
    dump(i);
}

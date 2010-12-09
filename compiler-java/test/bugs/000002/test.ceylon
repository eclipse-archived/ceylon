import com.redhat.ceylon.compiler.test.dump;

// The issue is that the 3 is never widened from a Natural,
// so trying to assign it to a non-mutable fails with a
// ClassCastException.

void test(Process process) {
    mutable Integer i = 3;
    Integer j = i;
    dump(j);
}

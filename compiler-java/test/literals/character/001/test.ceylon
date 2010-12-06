import com.redhat.ceylon.compiler.test.dump;

doc "Test character literal functionality"
void test(Process process) {
    dump(`1`);
    dump(`A`);
    dump(`#`);
    dump(` `);
    //TODO should this work? dump(```);
    //TODO should this work? dump(`\`);
    //TODO this should work! dump(`\\`);
    dump(`\``);
    dump(`\n`);
    dump(`\t`);
}

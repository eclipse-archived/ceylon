import com.redhat.ceylon.compiler.test.dump;

doc "Test Float literal functionality"
void test(Process process) {
    // Basic Float literals
    dump(23.0);
    dump(0.23);

    // Float literals with exponents
    dump(23.0e23);
    dump(0.23E+4);
    dump(17.4e-4);
    
    // Float literals with magnitudes
    //TODO dump(17.0p);
    //TODO dump(290.1n);
    //TODO dump(420.0u);
    //TODO dump(3.0m);
    //TODO dump(17.0k);
    //TODO dump(290.12M);
    //TODO dump(420.0G);
    //TODO dump(3.0T);

    // Float literals with underscores
    //TODO dump(1_123.435);
    //TODO dump(1_123.435_32);
    //TODO dump(1.123_435_3);

    // Float literals with underscores and exponents
    //TODO

    // Float literals with underscores and magnitudes
    //TODO
}

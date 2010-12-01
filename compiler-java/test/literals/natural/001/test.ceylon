import com.redhat.ceylon.compiler.test.dump;

doc "Test Natural literal functionality"
void test(Process process) {
    // Basic natural literal
    dump(23);

    // Basic Natural literals with magnitudes
    //TODO dump(17k);
    //TODO dump(29012M);
    //TODO dump(420G);
    //TODO dump(3T);

    // Natural literals with underscores
    //TODO dump(1_123_435);
    //TODO dump(12_123_435);
    //TODO dump(124_123_435);

    // Natural literals with underscores and magnitudes
    //TODO add some of these too
}

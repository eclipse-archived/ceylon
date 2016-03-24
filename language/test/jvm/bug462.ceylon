@test
shared void bug462() {
    printStackTrace(Exception(), (String s) => 1);
}
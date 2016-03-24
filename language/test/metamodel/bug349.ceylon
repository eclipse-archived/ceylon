@test
shared void bug349() {
    for (satisfiedType in `class Tuple`.satisfiedTypes) {
        assert(!satisfiedType.string.includes("given"));
    }
}

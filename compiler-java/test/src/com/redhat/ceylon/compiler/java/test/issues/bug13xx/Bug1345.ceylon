shared class Bug1345(shared String message) {}

shared void bug1345() {
    Integer[] aa = [];
    Bug1345 error = Bug1345(
        "`` "->".join(
            aa*.string
        ) ``");
}
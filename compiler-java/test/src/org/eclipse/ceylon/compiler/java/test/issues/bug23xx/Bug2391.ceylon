shared void bug2391() {
    value tuple = `class Tuple`.instantiate {
        typeArguments = [`String`, `String`, `[]`];
        arguments = ["head", []];
    };
}
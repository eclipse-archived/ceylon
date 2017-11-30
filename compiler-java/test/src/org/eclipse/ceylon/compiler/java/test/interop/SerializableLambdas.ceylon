
void serializableLambdas() {
    SerializableLambdas.serialize<Integer>((Integer left, Integer right) => left-right);
    SerializableLambdas.serialize<Integer>(function(Integer left, Integer right) => left-right);
}
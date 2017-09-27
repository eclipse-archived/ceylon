@noanno
class PrimitiveArrayIteration() {
    void byte() {
        List<Byte> byteArray = Array.ofSize(10, 0.byte);
        for (x in byteArray) {}
    }
    void boolean() {
        List<Boolean> boolArray = Array.ofSize(10, false);
        for (x in boolArray) {}
    }
    void integer() {
        List<Integer> integerArray = Array.ofSize(10, 0);
        for (x in integerArray) {}
    }
    void character() {
        List<Character> characterArray = Array.ofSize(10, '\0');
        for (x in characterArray) {}
    }
    void stringg() {
        List<String> stringArray = Array.ofSize(10, "");
        for (x in stringArray) {}
    }
    void float() {
        List<Float> floatArray = Array.ofSize(10, 0.0);
        for (x in floatArray) {}
    }
}

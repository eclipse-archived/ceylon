@nomodel
class KeywordInGetterObject() {
    Integer i {
        object assert {}
        String s = assert.string;
        return 0;
    }
}
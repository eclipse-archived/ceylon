@nomodel
class KeywordInSetterObject() {
    Integer i {
        return 0;
    }
    assign i {
        object assert {}
        String s = assert.string;
    }
}
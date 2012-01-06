@nomodel
class KeywordInClassObject() {
    object assert {}
    void m() {
        String s = assert.string;
    }
}
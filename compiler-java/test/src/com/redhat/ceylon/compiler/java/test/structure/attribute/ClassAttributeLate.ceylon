@noanno
class ClassAttributeLate() {
    shared late Integer lateAttr;
    shared variable late Integer lateVariableAttr;
    void init() {
        lateAttr = 0;
        lateVariableAttr = 0;
    }
}
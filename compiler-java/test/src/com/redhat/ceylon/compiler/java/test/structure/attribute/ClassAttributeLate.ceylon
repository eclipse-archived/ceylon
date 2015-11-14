@noanno
class ClassAttributeLate<T,U>() given U satisfies Object {
    shared late String lateStringAttr;
    shared variable late String lateStringVariableAttr;
    shared late Object lateNonNullAttr;
    shared variable late Object lateNonNullVariableAttr;
    shared late Object? lateOptionalAttr;
    shared variable late Object? lateOptionalVariableAttr;
    shared late T lateGenericAttr;
    shared variable late T lateGenericVariableAttr;
    shared late U lateGenericNonNullAttr;
    shared variable late U lateGenericNonNullVariableAttr;
    shared late Integer lateAttr;
    shared variable late Integer lateVariableAttr;
    void init() {
        lateAttr = 0;
        lateVariableAttr = 0;
    }
}
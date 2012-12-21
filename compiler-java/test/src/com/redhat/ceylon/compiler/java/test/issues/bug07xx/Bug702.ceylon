/* None doesn't exist anymore, not sure if we should find a similar example or jsut remove this issue
@nomodel
class Bug702() satisfies None<Integer> {
    shared actual Nothing last { return null; }
    shared actual Bug702 clone { return this; }
}
*/
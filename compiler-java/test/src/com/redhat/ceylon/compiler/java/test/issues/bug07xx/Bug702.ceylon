@nomodel
class Bug702() satisfies None<Integer> {
    shared actual Nothing last { return null; }
    shared actual Bug702 clone { return this; }
}

@noanno
interface SerializableMemberClassOfInterface {
    serializable class NonShared() {}
    shared serializable class Shared() {}
    shared default serializable class Default() {}
    shared formal serializable class Formal() {}
}
@noanno
abstract serializable class SerializableMemberClassOfClass() {
    serializable class NonShared() {}
    shared serializable class Shared() {}
    shared default serializable class Default() {}
    shared formal serializable class Formal() {}
}
@noanno
serializable class SerializableMemberClass() extends SerializableMemberClassOfClass() {
    shared actual serializable class Default() extends super.Default() {}
    shared actual serializable class Formal() extends super.Formal() {}
}
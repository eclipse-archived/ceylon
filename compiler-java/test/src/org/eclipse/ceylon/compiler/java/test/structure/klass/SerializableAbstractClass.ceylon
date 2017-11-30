@noanno
serializable
abstract class SerializableAbstractClass() {
    shared formal Boolean b;
}
@noanno
serializable
class SerializableAbstractClassSub() extends SerializableAbstractClass() {
    shared actual Boolean b => true;
}

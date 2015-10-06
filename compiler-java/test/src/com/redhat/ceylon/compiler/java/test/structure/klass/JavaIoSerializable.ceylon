class JavaIoSerializableObject() extends Object() {
    shared actual Boolean equals(Object other) => false;
    shared actual Integer hash => 0;
}

class JavaIoSerializableBasic() extends Basic() {}

object javaIoSerializableObject extends Basic() {}

object javaIoSerializableBasic extends Basic() {}

class JavaIoSerializableGeneric<T>() {}

interface JavaIoSerializable {
    shared default void m() {}
}

class JavaIoSerializableMixin() satisfies JavaIoSerializable {
}

class JavaIoSerializableValueCtors {
    shared new foo {}
    shared new bar {}
}

class Outer() {
    shared object javaIoSerializableObject extends Basic() {}
    shared class JavaIoSerializableValueCtors {
        shared new foo {}
        shared new bar {}
    }
}
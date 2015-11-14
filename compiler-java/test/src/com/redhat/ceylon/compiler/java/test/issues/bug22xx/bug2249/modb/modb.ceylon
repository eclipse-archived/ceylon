import com.redhat.ceylon.compiler.java.test.issues.bug22xx.bug2249.moda { InterfaceWithInitializedAttribute }

shared class ClassWithInitializedAttribute() satisfies InterfaceWithInitializedAttribute {
    shared actual String initialValue() => "Initial";
    shared actual variable String? internalValue = null;
}

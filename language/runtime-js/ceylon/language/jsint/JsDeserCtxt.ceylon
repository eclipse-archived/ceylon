import ceylon.language.serialization{DeserializationContext,Reference}
import ceylon.language.meta.model{Class,MemberClass}

native class JsDeserCtxt() satisfies DeserializationContext {

    shared actual native Reference<Instance> reference<Instance>(
        Object id, Class<Instance> clazz);
    shared actual native Reference<Instance> memberReference<Outer,Instance>(
        Object id, MemberClass<Outer,Instance> clazz,
        Reference<Outer>? outerReference);
    shared actual native Iterator<Reference<Object?>> iterator();
}

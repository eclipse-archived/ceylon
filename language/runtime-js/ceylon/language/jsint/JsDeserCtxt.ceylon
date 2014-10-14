import ceylon.language.serialization{DeserializationContext,Reference,RealizableReference,DeserializableReference}
import ceylon.language.meta.model{Class,MemberClass}

native class JsDeserCtxt() satisfies DeserializationContext {

  shared dynamic refs;
  shared dynamic ids;
  dynamic {
    refs = dynamic[];
    ids  = dynamic[];
  }
  shared actual native Reference<Instance> reference<Instance>(
        Object id, Class<Instance> clazz);
  shared actual native Reference<Instance> memberReference<Outer,Instance>(
        Object id, MemberClass<Outer,Instance> clazz,
        Reference<Outer>? outerReference);
  shared actual native Iterator<Reference<Object?>> iterator();
  shared native void put(Object id, DeserializableReference<Anything> ref);
  shared native Reference<Anything> update(Object id, RealizableReference<Anything> ref);
  shared native Reference<Anything> getReference(Object id);
}

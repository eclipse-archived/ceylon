import ceylon.language.serialization {SerializationContext,SerializableReference}

native class JsSerCtxt() satisfies SerializationContext {

  shared actual native SerializableReference<Instance> reference<Instance>(Object id, Instance instance);
  shared actual native Iterator<SerializableReference<Object?>> iterator();

}

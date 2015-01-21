import ceylon.language.serialization {SerializationContext,SerializableReference}

native class JsSerCtxt() satisfies SerializationContext {

  shared dynamic instances;
  shared dynamic refs;
  dynamic {
    instances=dynamic [\ithis];
    refs=dynamic [\ithis];
  }
  shared actual native SerializableReference<Instance> reference<Instance>(Object id, Instance instance);
  shared actual native Iterator<SerializableReference<Anything>> iterator();

  shared actual native Boolean contains(Object instance) {
    dynamic {
      return instances.indexOf(instance) >= 0;
    }
  }
  shared Object getId<Instance>(Instance instance) {
    dynamic {
      Integer idx=instances.indexOf(instance);
      if (idx >= 0) {
        return refs[idx].id;
      }
    }
    throw AssertionError("Instance ``instance else "NULL"`` has not been registered for initialization");
  }
  shared actual native SerializableReference<Instance>? getReference<Instance>(Instance instance);
}

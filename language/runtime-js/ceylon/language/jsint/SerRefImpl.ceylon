import ceylon.language.serialization{SerializableReference,Deconstructor}
import ceylon.language.meta.model{ClassModel}

native class SerRefImpl<Instance>(shared JsSerCtxt context, shared actual Object id, Instance inst)
    satisfies SerializableReference<Instance> {
  shared actual Instance instance() => inst;
  shared actual String string => "``id`` => ``inst else "null"``";
  shared actual native void serialize(Deconstructor deconstructor(ClassModel model));
  shared actual native ClassModel<Instance> clazz;
}

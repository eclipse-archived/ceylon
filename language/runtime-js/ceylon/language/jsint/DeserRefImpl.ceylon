import ceylon.language.serialization { DeserializableReference, RealizableReference, Deconstructed, Reference }
import ceylon.language.meta.model { ClassModel }

native class DeserRefImpl<Instance,Outer>(context, id, clazz, outerRef)
    satisfies DeserializableReference<Instance> & RealizableReference<Instance> {

  shared JsDeserCtxt context;
  shared actual Object id;
  shared actual ClassModel<Instance> clazz;
  shared Reference<Outer>? outerRef;

  shared actual native Instance instance();
  shared actual native void reconstruct();
  shared actual native RealizableReference<Instance> deserialize(/*Deconstructed<Instance>*/ Deconstructed deconstructed);
  shared actual native String string;
  shared native Instance leak();
}

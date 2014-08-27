"A stateless reference that can be deserialized to produce a [[RealizableReference]]."
shared sealed
interface DeserializableReference<Instance> satisfies Reference<Instance> {
    
    "Associate the given [[state]] with the instance, 
     returning a [[RealizableReference]]."
    shared formal RealizableReference<Instance> deserialize(/*Deconstructed<Instance>*/ Deconstructed deconstructed);
}

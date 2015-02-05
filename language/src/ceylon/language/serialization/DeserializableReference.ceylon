"A stateless reference to an instance that can be deserialized to 
 produce a [[RealizableReference]]."
shared sealed
interface DeserializableReference<out Instance> satisfies Reference<Instance> {
    
    "Associate the given [[state|deconstructed]] with the instance, 
     returning a [[RealizableReference]]."
    shared formal RealizableReference<Instance> deserialize(/*Deconstructed<Instance>*/ Deconstructed deconstructed);
}

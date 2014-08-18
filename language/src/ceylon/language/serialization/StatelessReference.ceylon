"A reference lacking sufficient state to realize an instance."
shared sealed
interface StatelessReference<Instance> satisfies Reference<Instance> {
    
    "Associate the given [[state]] with the instance, 
     returning a [[StatefulReference]]."
    shared formal StatefulReference<Instance> deserialize(/*Deconstructed<Instance>*/ Deconstructed deconstructed);
}

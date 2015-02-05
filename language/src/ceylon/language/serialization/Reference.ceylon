import ceylon.language.meta.model {
    ClassModel
}

"A reference to an instance of a class, with a certain 
 [[identifer|id]]."
shared sealed
interface Reference<out Instance> {
        //of SerializableReference<Instance> | RealizableReference<Instance> | DeserializableReference<Instance> {
    
    "The unique identifier of the instance."
    shared formal Object id;
    
    "The class of the instance."
    shared formal ClassModel<Instance> clazz;
}

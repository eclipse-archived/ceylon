"Thrown when the metamodel cannot be loaded, due to bugs in the metamodel loading, or 
 in the model descriptor.
 "
shared class ModelError(String? description=null, Throwable? cause=null) extends Error(description, cause){}


"A new [[SerializationContext]]."
shared native SerializationContext serialization();
shared native("jvm") SerializationContext serialization() 
        => SerializationContextImpl();



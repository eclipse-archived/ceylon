import com.redhat.ceylon.compiler.java.runtime.serialization{SerializationContextImpl}

"A new [[SerializationContext]]."
shared native SerializationContext serialization();

shared native("jvm") SerializationContext serialization()
        => SerializationContextImpl();

shared native("js") SerializationContext serialization()
        => nothing;
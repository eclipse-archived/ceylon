"Thrown to indicate an exception event during serialization"
shared class SerializationException(String message, Throwable? cause=null) 
        extends Exception(message, cause) {
}
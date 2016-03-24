"Thrown to indicate an exception event during deserialization"
shared class DeserializationException(String message, Throwable? cause=null) 
        extends Exception(message, cause) {
}
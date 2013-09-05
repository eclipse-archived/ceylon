"Rethrows an exception without declaring it. This is used as a cheap replacement for
 Unsafe.throwException() in our Java Util class, used by the metamodel."
by ("Stephane Epardaud")
shared void rethrow(Exception x){
    throw x;
}
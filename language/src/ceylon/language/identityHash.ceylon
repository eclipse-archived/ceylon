import java.lang { System }

"Return the system-defined identity hash value of the given 
 [[value|identifiable]]. This hash value is consistent with 
 [[identity equality|Identifiable.equals]]."
see (`function identical`)
shared native Integer identityHash(Identifiable identifiable);

shared native("jvm") 
Integer identityHash(Identifiable identifiable)
        => System.identityHashCode(identifiable);

shared native("js") 
Integer identityHash(Identifiable identifiable) {
    dynamic {
        dynamic x = identifiable;
        if (exists hash = x.\iBasicID) {
            return hash;
        }
        else {
            hash = \iBasicID++;
            x.\iBasicID = hash;
            return hash;
        }
    }
}
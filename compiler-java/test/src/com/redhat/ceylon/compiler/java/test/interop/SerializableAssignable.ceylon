import java.lang { 
    Integer,
    Long,
    Float,
    Double 
}
import java.io { Serializable }

shared void serializableAssignable() {
    variable Serializable s = Integer(0);
    s = Long(0);
    s = Float(0.0);
    s = Double(0.0);
}
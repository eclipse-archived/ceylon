shared abstract class BaseObject() 
        extends IdentifiableObject() {
        
    Attribute<subtype,Object>[] idAttributes() {
        return from ( Attribute<subtype,Object> a in type.attributes() ) 
                    where ( nonempty a.annotations(Id) );
    }
    
    doc "The equals operator |x == y|. Default implementation compares
         attributes annotated |id|, or performs identity comparison."
    see (id)
    shared default actual Boolean equals(IdentifiableObject that) {
        if (that.type!=type) {
            return false
        }
        Attribute<subtype,Object>[] idAttributes = idAttributes();
        if (nonempty idAttributes) {
            return equals(that, idAttributes)
        }
        else {
            return super.equals(that)
        }
    }
    
    doc "Compares the given attributes of this instance with the given
         attributes of the given instance."
    shared Boolean equals(IdentifiableObject that, Attribute<subtype,Object>... attributes) {
        if (is subtype that) {
            return forAll (Attribute<subtype,Object> a in attributes)
                        every ( a(this) == a(that) )
        }
        else {
            return false
        }
    }
    
    doc "The hash code of the instance. Default implementation compares
         attributes annotated |id|, or assumes identity equality."
    see (id)
    shared default actual Integer hash { 
        Attribute<subtype,Object>[] idAttributes = idAttributes();
        if (nonempty idAttributes) {
            return hash(idAttributes)
        }
        else {
            return super.hash
        }
    }
    
    doc "Computes the hash code of the instance using the given 
         attributes."
    shared Integer hash(Attribute<subtype,Object>... attributes) {
    	throw
    }
        
    doc "A developer-friendly string representing the instance. 
         By default, the string contains the name of the type, 
         and the values of all attributes annotated |id|."
    shared default actual String string {
        return string(idAttributes())
    }
    
    doc "A developer-friendly string representing the instance,
         containing the name of the type, and the value of the
         given attributes." 
    shared default String string(Attribute<subtype,Object>... attributes) {
        variable Character[] result := "";
        result .= with (type.name, "{");
        result .= forEach (Attribute<subtype,Object> a in attributes)
                    with ({ a.name, "=", $a(this), ";" });
        result .= with ("}");
        return result 
    }
        
}
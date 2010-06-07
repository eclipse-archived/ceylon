public abstract class Object {
        
    doc "The equals operator x == y. Default implementation compares
         attributes annotated |id|, or performs identity comparison."
    see #id
    public Boolean equals(Object that) { throw }
    
    doc "Compares the given attributes of this instance with the given
         attributes of the given instance."
    public Boolean equals(Object that, Attribute... attributes) { throw }
    
    doc "The hash code of the instance. Default implementation compares
         attributes annotated |id|, or assumes identity equality."
    see #id
    public Integer hash { throw }
    
    doc "Computes the hash code of the instance using the given 
         attributes."
    public Integer hash(Attribute... attributes) { throw }
        
    doc "The unary render operator $x. A developer-friendly string 
         representing the instance. By default, the string contains
         the name of the type, and the values of all attributes 
         annotated |id|."
    public String string { throw }
    
    doc "Determine if the instance belongs to the given |Category|."
    see #Category
    public Boolean in(Category cat) {
        return cat.contains(this)
    }
    
    doc "Determine if the instance belongs to the given |Iterable|
         object or listed objects."
    see #Iterable
    public Boolean in(Object... objects) {
        return forAny (Object elem in objects) some elem == this
    }
    
    doc "The |Type| of the instance."
    public Type<subtype> type { throw }
    
    doc "Binary assignability operator x is Y. Determine if the 
         instance is of the given |Type|."
    public Boolean instanceOf(Type<Object> type) {
        return this.type.assignableTo(type)
    }
    
    doc "A log obect for the type."
    public static Log log = Log(type);
        
    doc "Transform the given object to a string. Override to 
         customize the render operator and character string 
         template expression interpolation."
    public string(optional Object object) { 
        return (object ? "null").string;
    }

}
package ceylon.language.serialization;

import ceylon.language.Array;
import ceylon.language.Iterator;
import ceylon.language.finished_;
import ceylon.language.impl.BaseIterator;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.Object;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.runtime.serialization.Serializable;

@Ceylon(major=8)
@Object
class reach_ {

    private reach_() {}
    private static final reach_ value = new reach_();
    
    public static reach_ get_() {
        return value;
    }
    
    public String toString() {
        return "reach";
    }
    
    // shared native Anything get(ReachableReference ref);
    @TypeInfo("ceylon.language::Anything")
    public java.lang.Object getAnything(
            @Name("instance")
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object i, 
            @Name("ref")
            @TypeInfo("ceylon.language.serialization::ReachableReference")
            ReachableReference ref) {
        final Serializable instance = (Serializable)i;
        return instance.$get$(ref);
    }
    
    @TypeInfo("ceylon.language::Object")
    public java.lang.Object getObject(
            @Name("instance")
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object i, 
            @Name("ref")
            @TypeInfo("ceylon.language.serialization::ReachableReference")
            ReachableReference ref) {
        final Serializable instance = (Serializable)i;
        return instance.$get$(ref);
    }
    
    
    @TypeInfo("ceylon.language::Iterator<ceylon.language.serialization::ReachableReference>")
    public Iterator<? extends ReachableReference> references(
            @Name("instance")
            @TypeInfo("ceylon.language::Anything")
            java.lang.Object i) {
        final Serializable instance = (Serializable)i;
        return new BaseIterator<ReachableReference>(ReachableReference.$TypeDescriptor$) {
            java.util.Iterator<ReachableReference> it = instance.$references$().iterator();
            int index = 0;
            @Override
            public java.lang.Object next() {
                if (it.hasNext()) {
                    return it.next();
                } else if (instance instanceof Array
                        && index < ((Array<?>)instance).getSize()) {
                    Element result = new ElementImpl(index);
                    index++;
                    return result;
                } else {
                    return finished_.get_();
                }
            }
        };
    }
    
}

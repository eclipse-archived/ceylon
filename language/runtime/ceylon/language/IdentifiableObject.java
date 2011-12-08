package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.Class;
import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;
import com.redhat.ceylon.compiler.metadata.java.Util;

@Ceylon
@SatisfiedTypes("ceylon.language.Equality")
@Class(extendsType="ceylon.language.Object")
public abstract class IdentifiableObject {

    public boolean equals(@Name("that") 
    @TypeInfo("ceylon.language.Equality") 
    java.lang.Object that) {
        if (Util.isIdentifiableObject(that)) {
            return this == that;
        }
        else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public java.lang.String toString() {
        return super.toString();
    }
}
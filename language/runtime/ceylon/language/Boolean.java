package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.CaseTypes;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Class;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.ValueType;
import com.redhat.ceylon.compiler.java.runtime.model.ReifiedType;
import com.redhat.ceylon.compiler.java.runtime.model.TypeDescriptor;

@Ceylon(major = 6)
@Class
@CaseTypes({"ceylon.language::true", "ceylon.language::false"})
@ValueType
public abstract class Boolean implements ReifiedType {

    @Ignore
    public final static TypeDescriptor $TypeDescriptor$ = TypeDescriptor.klass(Boolean.class);

    @Ignore
    public static Boolean instance(boolean value) {
        return value ? true_.get_() : false_.get_();
    }

    @Ignore
    abstract public boolean booleanValue();
    
    @Ignore
    public static java.lang.String toString(boolean value) {
        return value ? "true" : "false";
    }
    
    @Ignore
    public static boolean equals(boolean value, java.lang.Object that) {
        if (that instanceof Boolean) {
            return value == ((Boolean) that).booleanValue();
        }
        else {
            return false;
        }
    }

    @Override
    @Ignore
    public final int hashCode() {
        return hashCode(booleanValue());
    }

    @Ignore
    public static int hashCode(boolean value) {
        return value ? 1231 : 1237;
    }

    @Override
    @Ignore
    public final TypeDescriptor $getType$() {
        return $TypeDescriptor$;
    }
    
}

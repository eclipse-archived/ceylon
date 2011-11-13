package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon @Attribute
public class bottom {
    @TypeInfo("ceylon.language.Bottom")
    public static <T> T getBottom(){
        throw new Exception(null, null);
    }
}
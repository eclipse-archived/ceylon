package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 5)
@Method
public final class string_ {
    
    private string_() {}
    
    @TypeInfo("ceylon.language::String")
    public static java.lang.String string(@Name("characters")
    @TypeInfo("ceylon.language::Iterable<ceylon.language::Character,ceylon.language::Null>")
    final Iterable<? extends Character, ? extends java.lang.Object> characters) {
        if (characters instanceof String) {
            return ((String)characters).toString();
        } else {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            java.lang.Object $tmp;
            for (Iterator<? extends Character> $val$iter$0 = characters.iterator(); 
                    !(($tmp = $val$iter$0.next()) instanceof Finished);) {
                sb.append($tmp);
            }
            return sb.toString();
        }
    }
}

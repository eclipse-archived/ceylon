package ceylon.language;

import com.redhat.ceylon.compiler.java.language.AbstractCallable;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Method;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;

@Ceylon(major = 3)
@Method
public class forKey_ {
    private forKey_(){}

    @TypeParameters({@TypeParameter(value="Key", satisfies="ceylon.language::Object"),
    		        @TypeParameter(value="Result")})
    @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<ceylon.language::Entry<Key,ceylon.language::Object>,ceylon.language::Entry<Key,ceylon.language::Object>,ceylon.language::Empty>>")
    public static 
    <Key,Result> Callable<? extends Result> forKey(
            @Name("resulting") @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Key,Key,ceylon.language::Empty>>")
            final Callable<? extends Result> resulting) {
        return new AbstractCallable<Result>("forKey"){
            public Result $call(java.lang.Object entry) {
                return resulting.$call(((Entry<? extends Key,?>)entry).getKey());
            }
        };
    }
}

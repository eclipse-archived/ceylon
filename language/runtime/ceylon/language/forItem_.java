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
public class forItem_ {
    private forItem_(){}

    @TypeParameters({@TypeParameter(value="Item", satisfies="ceylon.language::Object"),
    		        @TypeParameter(value="Result")})
    @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Empty>>")
    public static 
    <Item,Result> Callable<? extends Result> forItem(
            @Name("resulting") @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Item,Item,ceylon.language::Empty>>")
            final Callable<? extends Result> resulting) {
        return new AbstractCallable<Result>("forItem"){
            public Result $call(java.lang.Object entry) {
                return resulting.$call(((Entry<?,? extends Item>)entry).getItem());
            }
        };
    }
}

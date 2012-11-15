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
public class byItem_ {

    private byItem_(){}

    @TypeParameters(@TypeParameter(value="Item", satisfies="ceylon.language::Object"))
    @TypeInfo("ceylon.language::Callable<ceylon.language::Nothing|ceylon.language::Comparison,ceylon.language::Tuple<ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Tuple<ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Entry<ceylon.language::Object,Item>,ceylon.language::Empty>>>")
    public static <Element> Callable<? extends Comparison> byItem(
            @Name("comparing")
            @TypeInfo("ceylon.language::Callable<ceylon.language::Nothing|ceylon.language::Comparison,ceylon.language::Tuple<Item,Item,ceylon.language::Tuple<Item,Item,ceylon.language::Empty>>>")
            final Callable<? extends Comparison> comparing) {
        return new AbstractCallable<Comparison>("byItem") {
            public Comparison $call(java.lang.Object x, java.lang.Object y) {
                return comparing.$call(((Entry)x).getItem(), ((Entry)y).getItem());
            }
            
        };
    }
}

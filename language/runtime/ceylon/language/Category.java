package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Name;
import com.redhat.ceylon.compiler.metadata.java.Sequenced;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public interface Category {
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Equality") Object element);
    
    //TODO: @TypeInfo
    public boolean containsEvery(@Sequenced @Name("elements") Iterable<? extends Object> elements);

    //TODO: @TypeInfo
    public boolean containsAny(@Sequenced @Name("elements") Iterable<? extends Object> elements);
}

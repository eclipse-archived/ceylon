package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Annotation;
import com.redhat.ceylon.compiler.java.metadata.Annotations;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon(major = 3)
@TypeParameters({@TypeParameter(value="Key", variance=Variance.OUT,
                               satisfies="ceylon.language::Object"),
                 @TypeParameter(value="Item", variance=Variance.OUT, 
                               satisfies="ceylon.language::Object")})
@SatisfiedTypes({"ceylon.language::Collection<ceylon.language::Entry<Key,Item>>",
                 "ceylon.language::Correspondence<ceylon.language::Object,Item>",
                 "ceylon.language::Cloneable<ceylon.language::Map<Key,Item>>"})
public interface Map<Key,Item> 
        extends Correspondence<java.lang.Object, Item>, 
                Collection<Entry<? extends Key,? extends Item>> {

    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language::Object")
    java.lang.Object that);
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    public int hashCode();
    
    @Annotations({@Annotation("actual"), @Annotation("default")})
    @Override
    @TypeInfo("ceylon.language::Set<Key>")
    public Set<? extends Key> getKeys();
    
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Collection<Item>")
    public Collection<? extends Item> getValues();
    
    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Map<Item,ceylon.language::Set<Key>>")
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse();

    @Annotations(@Annotation("default"))
    @TypeInfo("ceylon.language::Map<Key,Result>")
    @TypeParameters(@TypeParameter(value="Result", satisfies="ceylon.language::Object"))
    public <Result> Map<? extends Key, ? extends Result> mapItems(
            @Name("mapping") @TypeInfo("ceylon.language::Callable<Result,ceylon.language::Tuple<Key|Item,Key,ceylon.language::Tuple<Item,Item,ceylon.language::Empty>>>")
            Callable<? extends Result> mapping);

}

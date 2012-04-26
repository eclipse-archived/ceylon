package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters({@TypeParameter(value = "Key", variance = Variance.OUT,
                               satisfies = "ceylon.language.Object"),
                 @TypeParameter(value = "Item", variance = Variance.OUT, 
                               satisfies = "ceylon.language.Object")})
@SatisfiedTypes({"ceylon.language.Collection<ceylon.language.Entry<Key,Item>>",
                 "ceylon.language.Correspondence<ceylon.language.Object,Item>",
                 "ceylon.language.Cloneable<ceylon.language.Map<Key,Item>>"})
public interface Map<Key,Item> 
        extends Correspondence<java.lang.Object, Item>, 
                Collection<Entry<? extends Key,? extends Item>> {

    @Override
    public long count(@Name("element") @TypeInfo("ceylon.language.Object") 
    java.lang.Object element);
    
    @Override
    public boolean equals(@Name("that") @TypeInfo("ceylon.language.Object")
    java.lang.Object that);
    
    @Override
    public int hashCode();
    
    @Override
    @TypeInfo("ceylon.language.Set<Key>")
    public Set<? extends Key> getKeys();
    
    @TypeInfo("ceylon.language.Collection<Item>")
    public Collection<? extends Item> getValues();
    
    @TypeInfo("ceylon.language.Map<Item,ceylon.language.Set<Key>>")
    public Map<? extends Item, ? extends Set<? extends Key>> getInverse();

}

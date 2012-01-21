package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters({@TypeParameter(value = "Key",
                               satisfies = "ceylon.language.Equality"),
                 @TypeParameter(value = "Element", variance = Variance.OUT, 
                               satisfies = "ceylon.language.Equality")})
@SatisfiedTypes({"ceylon.language.Collection<ceylon.language.Entry<Key,Item>>",
                 "ceylon.language.Correspondence<Key,Item>",
                 "ceylon.language.Cloneable<ceylon.language.Map<Key,Item>>"})
public interface Map<Key,Item> 
        extends Correspondence<Key,Item>, 
                Collection<Entry<Key,Item>> {

}

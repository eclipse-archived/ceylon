package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
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
	
    @Ignore
    public static final class Map$impl {
        public static <Key,Item> boolean equals(final Map<Key,Item> $this, java.lang.Object that) {
        	if (that instanceof Map) {
        		Map other = (Map) that;
        		if (other.getSize()==$this.getSize()) {
                    java.lang.Object elem;
                    for (ceylon.language.Iterator<? extends Entry<Key,Item>> iter = $this.getIterator(); 
                    		!((elem = iter.next()) instanceof Finished);) {
                    	Entry<Key,Item> entry = (Entry<Key,Item>) elem;
                    	java.lang.Object y = other.item(entry.getKey());
                    	Item x = entry.getItem();
        				if (x==y || x!=null && y!=null && x.equals(y)) {
        					continue;
        				}
        				return false;
        			}
        			return true;
        		}
        	}
        	return false;
        }
    }

}

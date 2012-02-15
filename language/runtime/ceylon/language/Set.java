package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;
import com.redhat.ceylon.compiler.java.metadata.TypeParameter;
import com.redhat.ceylon.compiler.java.metadata.TypeParameters;
import com.redhat.ceylon.compiler.java.metadata.Variance;

@Ceylon
@TypeParameters(@TypeParameter(value = "Element", variance = Variance.OUT, 
                               satisfies = "ceylon.language.Object"))
@SatisfiedTypes({"ceylon.language.Collection<Element>",
                 "ceylon.language.Cloneable<ceylon.language.Set<Element>>"})
public interface Set<Element> 
        extends Collection<Element> {

    public boolean superset(@TypeInfo("ceylon.language.Set<ceylon.language.Object>") 
                            @Name("set") Set<? extends java.lang.Object> set);
    public boolean subset(@TypeInfo("ceylon.language.Set<ceylon.language.Object>") 
                          @Name("set") Set<? extends java.lang.Object> set);
    
    @TypeInfo("ceylon.language.Set<Element|Other>")
    public <Other> Set union(@TypeInfo("ceylon.language.Set<Other>") 
                             @Name("set") Set<? extends Other> set);
    
    @TypeInfo("ceylon.language.Set<Element&Other>")
    public <Other> Set intersection(@TypeInfo("ceylon.language.Set<Other>") 
                                    @Name("set") Set<? extends Other> set);

    @TypeInfo("ceylon.language.Set<Element|Other>")
    public <Other> Set exclusiveUnion(@TypeInfo("ceylon.language.Set<Other>") 
                                      @Name("set") Set<? extends Other> set);
    
    @TypeInfo("ceylon.language.Set<Element>")
    public <Other> Set<? extends Element> complement(@TypeInfo("ceylon.language.Set<Other>") 
                                      @Name("set") Set<? extends Other> set);
    
    @Ignore
    public static final class Set$impl {
        
        public static <Element> long count(final Set<Element> $this, java.lang.Object element) {
            return $this.contains(element) ? 1 : 0;
        }
        
        public static <Element> boolean superset(Set<Element> $this, Set<? extends java.lang.Object> set) {
            java.lang.Object elem;
            for (ceylon.language.Iterator<? extends java.lang.Object> iter = set.getIterator(); 
                    !((elem = iter.next()) instanceof Finished);) {
                if (!$this.contains(elem)) return false;
            }
            return true;
        }
        
        public static <Element> boolean subset(Set<Element> $this, Set<? extends java.lang.Object> set) {
            java.lang.Object elem;
            for (ceylon.language.Iterator<? extends Element> iter = $this.getIterator(); 
                    !((elem = iter.next()) instanceof Finished);) {
                if (!set.contains(elem)) return false;
            }
            return true;
        }
        
        public static <Element> boolean equals(final Set<Element> $this, java.lang.Object that) {
        	if (that instanceof Set) {
        		Set other = (Set) that;
        		if (other.getSize()==$this.getSize()) {
                    java.lang.Object elem;
                    for (ceylon.language.Iterator<? extends Element> iter = $this.getIterator(); 
                    		!((elem = iter.next()) instanceof Finished);) {
                    	if (!other.contains(elem)) return false;
        			}
        			return true;
        		}
        	}
        	return false;
        }
        public static <Element> int hashCode(final Set<Element> $this) {
            return (int) $this.getSize();
        }
    }

}

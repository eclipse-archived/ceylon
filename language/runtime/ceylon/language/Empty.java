package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.Name;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@SatisfiedTypes({
    "ceylon.language.List<ceylon.language.Bottom>",
    "ceylon.language.None<ceylon.language.Bottom>",
    "ceylon.language.Ranged<ceylon.language.Integer,ceylon.language.Empty>",
    "ceylon.language.Cloneable<ceylon.language.Empty>"
})
public interface Empty 
        extends List, None {
	
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Integer getLastIndex();
    
    @Override
    @TypeInfo("ceylon.language.Integer")
    public long getSize(); 
    
    @Override
    public boolean getEmpty();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") java.lang.Object key);
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object getFirst();
    
    @Ignore
    public static final class Empty$impl {
        public static long getSize(Empty $this){
            return 0;
        }
        public static boolean getEmpty(Empty $this){
            return true;
        }
        public static Iterator<java.lang.Object> getIterator(Empty $this){
            return new Iterator<java.lang.Object>() {
                @Override
                public java.lang.Object next() {
                    return exhausted.getExhausted();
                }
            };
        }
        public static java.lang.Object item(Empty $this, Integer key){
            return null;
        }
        public static Empty segment(Empty $this, Comparable from, Comparable length) {
            return $this;
        }
        public static Empty span(Empty $this, Comparable from, Comparable to) {
            return $this;
        }
        public static long count(Empty $this, java.lang.Object element) {
            return 0;
        }
        public static boolean contains(Empty $this, java.lang.Object element) {
            return false;
        }
        public static boolean defines(Empty $this, Integer index) {
            return false;
        }
        public static Cloneable getClone(Empty $this) {
            return $this;
        }
        public static java.lang.Object getFirst(Empty $this){
            return null;
        }
        public static java.lang.String toString(Empty $this) {
            return "{}";
        }
        public static Integer getLastIndex(Empty $this){
            return null;
        }
    }
}

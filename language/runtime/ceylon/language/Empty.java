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
    public long getSize(); 
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Iterator getIterator();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public java.lang.Object item(@Name("key") @TypeInfo("ceylon.language.Integer")
    Integer key);

    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty segment(@Name("from") @TypeInfo("ceylon.language.Integer")
    Integer from,
    @Name("length") @TypeInfo("ceylon.language.Integer")
    Integer length);

    @Override
    @TypeInfo("ceylon.language.Empty")
    public Empty span(@Name("from") @TypeInfo("ceylon.language.Integer")
    Integer from,
    @Name("to") @TypeInfo("ceylon.language.Integer|ceylon.language.Nothing")
    Integer length);

    @Override
    public java.lang.String toString();
    
    @Override
    @TypeInfo("ceylon.language.Nothing")
    public Integer getLastIndex();

    @Override
    public Empty getClone();
    
    @Override
    public boolean contains(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Override
    public long count(@Name("element") @TypeInfo("ceylon.language.Object")
    java.lang.Object element);

    @Override
    public boolean defines(@Name("index") @TypeInfo("ceylon.language.Integer")
    Integer index);

    @Ignore
    public static final class Empty$impl {
        public static long getSize(Empty $this){
            return 0;
        }
        public static Iterator getIterator(Empty $this){
            return emptyIterator.getEmptyIterator();
        }
        public static java.lang.Object item(Empty $this, java.lang.Object key){
            return null;
        }
        public static Empty segment(Empty $this, Integer from, Integer length) {
            return $this;
        }
        public static Empty span(Empty $this, Integer from, Integer to) {
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
        public static Empty getClone(Empty $this) {
            return $this;
        }
        public static java.lang.String toString(Empty $this) {
            return "{}";
        }
        public static Integer getLastIndex(Empty $this){
            return null;
        }
    }
}

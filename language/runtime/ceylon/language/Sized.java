package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.Ignore;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;

@Ceylon
@SatisfiedTypes("ceylon.language.Container")
public interface Sized extends Container {

    public long getSize();
    
    public boolean getEmpty();
    
    @Ignore
    public static final class Sized$impl {
        
        public static final boolean getEmpty(Sized $this){
            return $this.getSize() == 0;
        }
        
    }
}

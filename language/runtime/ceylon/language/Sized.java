package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.SatisfiedTypes;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@SatisfiedTypes("ceylon.language.Container")
public interface Sized extends Container {

    @TypeInfo("ceylon.language.Natural")
    public long getSize();
    
    public boolean getEmpty();/* {
        return size==0;
    }*/

}

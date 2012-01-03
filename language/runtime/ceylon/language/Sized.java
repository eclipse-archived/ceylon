package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.SatisfiedTypes;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@SatisfiedTypes("ceylon.language.Container")
public interface Sized extends Container {

    @TypeInfo("ceylon.language.Integer")
    public long getSize();
    
    public boolean getEmpty();/* {
        return size==0;
    }*/

}

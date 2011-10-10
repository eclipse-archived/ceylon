package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

public interface Sized extends Container {

    @TypeInfo("ceylon.language.Natural")
    public long getSize();
    
    public boolean getEmpty();/* {
        return size==0;
    }*/

}

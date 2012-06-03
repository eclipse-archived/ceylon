package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon
@Attribute
@com.redhat.ceylon.compiler.java.metadata.Module(name = "ceylon.language", version = "0.3", dependencies = {})
final class module {
    
    @TypeInfo("ceylon.language.descriptor.Module")
    static ceylon.language.descriptor.Module getModule() {
        return new ceylon.language.descriptor.Module(
        		ceylon.language.Quoted.instance("ceylon.language"), 
        		ceylon.language.Quoted.instance("0.3"), 
        		"The Ceylon language module containing the core types referred to in the language specification.", 
        		new ceylon.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King")), 
        		ceylon.language.Quoted.instance("http://www.apache.org/licenses/LICENSE-2.0.html"), 
        		$empty.getEmpty());
    }
    
    private module() {
    }
}
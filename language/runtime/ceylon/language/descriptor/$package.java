package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Attribute
@com.redhat.ceylon.compiler.metadata.java.Package(name = "ceylon.language.descriptor", shared = true)
final class $package {
    
    @TypeInfo("ceylon.language.descriptor.Package")
    static ceylon.language.descriptor.Package getPackage() {
        return new ceylon.language.descriptor.Package(ceylon.language.Quoted.instance("ceylon.language.descriptor"), 
        		true, 
        		"The Ceylon Language Descriptor package",
        		new ceylon.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King")));
    }
    
    private $package() {
    }
}
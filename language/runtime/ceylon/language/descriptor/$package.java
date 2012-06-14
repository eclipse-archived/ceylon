package ceylon.language.descriptor;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 1)
@Attribute
@com.redhat.ceylon.compiler.java.metadata.Package(name = "ceylon.language.descriptor", shared = true)
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
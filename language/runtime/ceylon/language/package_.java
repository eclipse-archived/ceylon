package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Attribute
@com.redhat.ceylon.compiler.java.metadata.Package(name = "ceylon.language", shared = true)
final class package_ {
    
    @TypeInfo("ceylon.language.descriptor::Package")
    static ceylon.language.descriptor.Package getPackage$() {
        return new ceylon.language.descriptor.Package("ceylon.language", 
        		true, 
        		"The Ceylon Language package",
        		new com.redhat.ceylon.compiler.java.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King")));
    }
    
    private package_() {
    }
}
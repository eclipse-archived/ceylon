package ceylon.language;

import com.redhat.ceylon.compiler.java.metadata.Attribute;
import com.redhat.ceylon.compiler.java.metadata.Ceylon;
import com.redhat.ceylon.compiler.java.metadata.TypeInfo;

@Ceylon(major = 3)
@Attribute
@com.redhat.ceylon.compiler.java.metadata.Module(name = "ceylon.language", version = "0.4", dependencies = {}, 
    doc="The Ceylon language module containing the core types referred to in the language specification.",
    by = {"Gavin King", "Tom Bentley", "Tako Schotanus", "Stephane Epardaud", "Enrique Zamudio"}, 
    license="http://www.apache.org/licenses/LICENSE-2.0.html")
final class module_ {
    
    @TypeInfo("ceylon.language.descriptor.Module")
    static ceylon.language.descriptor.Module getModule$() {
        return new ceylon.language.descriptor.Module(
        		"ceylon.language", 
        		"0.4", 
        		"The Ceylon language module containing the core types referred to in the language specification.", 
        		new com.redhat.ceylon.compiler.java.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King")), 
        		"http://www.apache.org/licenses/LICENSE-2.0.html", 
        		(List)empty_.getEmpty$());
    }
    
    private module_() {
    }
}
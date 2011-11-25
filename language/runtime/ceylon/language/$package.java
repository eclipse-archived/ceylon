package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Attribute
@com.redhat.ceylon.compiler.metadata.java.Package(name = "ceylon.language", shared = true)
final class $package {
    
    @TypeInfo("ceylon.language.descriptor.Package")
    static ceylon.language.descriptor.Package getPackage() {
        return new ceylon.language.NamedArgumentCall<java.lang.Void>(null, 
                ceylon.language.Quoted.instance("ceylon.language"),
                ceylon.language.Boolean.instance(true), 
                ceylon.language.String.instance("The Ceylon Language package"), 
                new ceylon.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King"))){
            
            public ceylon.language.descriptor.Package call$() {
                return new ceylon.language.descriptor.Package((ceylon.language.Quoted)this.args[0], 
                        ((ceylon.language.Boolean)this.args[1]).booleanValue(), 
                        (ceylon.language.Iterable<? extends ceylon.language.String>)this.args[3], 
                        ((ceylon.language.String)this.args[2]).toString());
            }
        }.call$();
    }
    
    private $package() {
    }
}
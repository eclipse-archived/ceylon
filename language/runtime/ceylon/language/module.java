package ceylon.language;

import com.redhat.ceylon.compiler.metadata.java.Attribute;
import com.redhat.ceylon.compiler.metadata.java.Ceylon;
import com.redhat.ceylon.compiler.metadata.java.TypeInfo;

@Ceylon
@Attribute
@com.redhat.ceylon.compiler.metadata.java.Module(name = "ceylon.language", version = "0.1", dependencies = {})
final class module {
    
    @TypeInfo("ceylon.language.descriptor.Module")
    static ceylon.language.descriptor.Module getModule() {
        return new ceylon.language.NamedArgumentCall<java.lang.Void>(null, 
                ceylon.language.Quoted.instance("ceylon.language"), 
                ceylon.language.Quoted.instance("0.1"), 
                ceylon.language.String.instance("The Ceylon language module containing the core types referred to in the language specification."), 
                new ceylon.language.ArraySequence<ceylon.language.String>(ceylon.language.String.instance("Gavin King")),
                ceylon.language.Quoted.instance("http://www.apache.org/licenses/LICENSE-2.0.html"), 
                new ceylon.language.ArraySequence<ceylon.language.descriptor.Import>()){
            
            public ceylon.language.descriptor.Module call$() {
                return new ceylon.language.descriptor.Module((ceylon.language.Quoted)this.args[0], (ceylon.language.Quoted)this.args[1], (ceylon.language.String)this.args[2], (ceylon.language.Iterable<? extends ceylon.language.String>)this.args[3], (ceylon.language.Quoted)this.args[4], (ceylon.language.Iterable<? extends ceylon.language.descriptor.Import>)this.args[5]);
            }
        }.call$();
    }
    
    private module() {
    }
}
import ceylon.language.meta.declaration{
    ClassWithInitializerDeclaration
}
import ceylon.language.meta{
    type
}
import ceylon.language.meta.model{
    StorageException
}

@test
shared void bug6364() {
    class C {
        shared new c {}
        shared new d() {}
    }
 
    value x1 = `C.c`;
    try{
        x1.get();
        if (runtime.name=="jvm") {
            "JVM does not support this, see #6364"
            assert(false);
        }
    }catch(StorageException x){
        // success
    }
    value x2 = `C.d`;
}

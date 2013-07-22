import com.redhat.ceylon.compiler.typechecker.test.moduleWithExportedType { ExportedClass, ExportedInterface }

//
// Type signatures

shared class SharedTypeInParam(@error ExportedClass a){}
class TypeInParam(ExportedClass a){}

@error shared class SharedTypeInParam2(a){
    @error shared ExportedClass a;
}
class TypeInParam2(a){
    shared ExportedClass a;
}

@error
shared class SharedTypeInExtends() extends ExportedClass(){}
class TypeInExtends() extends ExportedClass(){}

@error
shared class SharedTypeInSatisfies() satisfies ExportedInterface{}
class TypeInSatisfies() satisfies ExportedInterface{}

// FIXME: bounds on the type param fails because the type param is not exported, is that correct?
//@error
//shared class SharedTypeInTypeParamBounds<T>() given T satisfies ExportedInterface{}
//class TypeInTypeParamBounds<T>() given T satisfies ExportedInterface{}

// FIXME: are there other things in the signature of a class we should check? case types?

//
// Type members

shared class SharedTypeInBody(){
    @error shared ExportedClass sharedAttr = nothing;
    ExportedClass privateAttr = nothing;

    if(true){
        // this is not exported so it's allowed
        ExportedClass c = nothing;
    }

    @error shared ExportedClass sharedGetter1 => nothing;
    ExportedClass privateGetter1 => nothing;

    @error shared ExportedClass sharedGetter2 { return nothing; }
    ExportedClass privateGetter2 { return nothing; }
    
    @error shared ExportedClass sharedMethod(@error ExportedClass a){ return a; }
    ExportedClass privateMethod(ExportedClass a){ return a; }

    @error shared ExportedClass sharedMethod2(@error ExportedClass a) => sharedMethod(a);
    ExportedClass privateMethod2(ExportedClass a) => privateMethod(a);

    @error shared ExportedClass|ExportedInterface sharedMethod3(@error ExportedClass|ExportedInterface a){ return a; }
    ExportedClass|ExportedInterface privateMethod3(ExportedClass|ExportedInterface a){ return a; }
    
    shared class SharedInner(@error ExportedClass a){}
    class PrivateInner(ExportedClass a){}
}
class TypeInBody(){
    shared ExportedClass sharedAttr = nothing;
    ExportedClass privateAttr = nothing;

    if(true){
        // this is not exported so it's allowed
        ExportedClass c = nothing;
    }

    shared ExportedClass sharedGetter1 => nothing;
    ExportedClass privateGetter1 => nothing;

    shared ExportedClass sharedGetter2 { return nothing; }
    ExportedClass privateGetter2 { return nothing; }
    
    shared ExportedClass sharedMethod(ExportedClass a){ return a; }
    ExportedClass privateMethod(ExportedClass a){ return a; }

    shared ExportedClass sharedMethod2(ExportedClass a) => sharedMethod(a);
    ExportedClass privateMethod2(ExportedClass a) => privateMethod(a);
    
    shared ExportedClass|ExportedInterface sharedMethod3(ExportedClass|ExportedInterface a){ return a; }
    ExportedClass|ExportedInterface privateMethod3(ExportedClass|ExportedInterface a){ return a; }
    
    shared class SharedInner(ExportedClass a){}
    class PrivateInner(ExportedClass a){}
}

//
// Toplevel attributes/methods

@error shared ExportedClass sharedAttr = nothing;
ExportedClass privateAttr = nothing;

@error shared ExportedClass sharedGetter1 => nothing;
ExportedClass privateGetter1 => nothing;

@error shared ExportedClass sharedGetter2 { return nothing; }
ExportedClass privateGetter2 { return nothing; }

@error shared ExportedClass sharedMethod(@error ExportedClass a){ return a; }
ExportedClass privateMethod(ExportedClass a){ return a; }

@error shared ExportedClass sharedMethod2(@error ExportedClass a) => sharedMethod(a);
ExportedClass privateMethod2(ExportedClass a) => privateMethod(a);

@error shared ExportedClass|ExportedInterface sharedMethod3(@error ExportedClass|ExportedInterface a){ return a; }
ExportedClass|ExportedInterface privateMethod3(ExportedClass|ExportedInterface a){ return a; }

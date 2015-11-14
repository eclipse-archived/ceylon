import com.redhat.ceylon.compiler.typechecker.test.moduleWithExportedType { ExportedClass, ExportedInterface }

//
// Type signatures

shared class SharedTypeInParam(ExportedClass a){}
class TypeInParam(ExportedClass a){}

shared class SharedTypeInParam2(a){
    shared ExportedClass a;
}
class TypeInParam2(a){
    shared ExportedClass a;
}

shared class SharedTypeInExtends() extends ExportedClass(){}
class TypeInExtends() extends ExportedClass(){}

shared class SharedTypeInSatisfies() satisfies ExportedInterface{}
class TypeInSatisfies() satisfies ExportedInterface{}

// FIXME: bounds on the type param fails because the type param is not exported, is that correct?
//
//shared class SharedTypeInTypeParamBounds<T>() given T satisfies ExportedInterface{}
//class TypeInTypeParamBounds<T>() given T satisfies ExportedInterface{}

// FIXME: are there other things in the signature of a class we should check? case types?

//
// Type members

shared class SharedTypeInBody(){
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

shared ExportedClass sharedAttr = nothing;
ExportedClass privateAttr = nothing;

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

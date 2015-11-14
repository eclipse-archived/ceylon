import ceylon.language.meta.declaration{...}
import ceylon.language.meta { modules }

shared final annotation class Bug776Annotation() satisfies OptionalAnnotation<Bug776Annotation> {}
shared annotation Bug776Annotation bug776Annotation() => Bug776Annotation();

bug776Annotation
void bug776Unshared(){
    print("Called");
}

@test
shared void bug776() {
    for(m in modules.list) {
        if( "modules.imported".startsWith(m.name) ) {
            print("Found module: ``m``");
            if( exists p = m.findPackage("modules.imported")) {
                print("Found package: ``p``");
                value decl = p.getMember<FunctionDeclaration|ClassDeclaration>("unsharedFunction");
                print("Decl: ``(decl else "null")``");
                if (is FunctionDeclaration decl) {
                    if (decl.toplevel) { // I THINK this is where it fails
                        print("Found it");
                    }
                }
            }
        }
    }
    
}
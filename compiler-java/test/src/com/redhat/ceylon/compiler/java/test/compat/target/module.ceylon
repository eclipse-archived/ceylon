"compiled with ceylon 1.2.1 compiler with --target=1.2.0 we should generate
 a car which runs in ceylon 1.2.0 runtime. 
 
 This means we must generate a module descriptor with appropriate 1.2.0 imports
 (even if we typecheck using 1.2.1 dist modules)."
native("jvm") module com.redhat.ceylon.compiler.java.test.compat.target "1.0.0" {
    import ceylon.bootstrap "1.2.0";
    import ceylon.runtime "1.2.0";
    //import ceylon.language "1.2.0";
    import com.redhat.ceylon.common "1.2.0";
    import com.redhat.ceylon.typechecker "1.2.0";
    import "com.redhat.ceylon.module-resolver" "1.2.0";
    import com.redhat.ceylon.compiler.java "1.2.0";
    import com.redhat.ceylon.compiler.js "1.2.0";
    import com.redhat.ceylon.model "1.2.0";
}
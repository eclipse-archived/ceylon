"This module should be compiled with the 1.2.1 compiler
 and this have dependencies only on 1.2.1 dist modules"
native("jvm") module compiled121 "1.0.0" {
    import ceylon.bootstrap "1.2.1";
    import ceylon.runtime "1.2.1";
    //import ceylon.language "1.2.1";
    import org.eclipse.ceylon.common "1.2.1";
    import org.eclipse.ceylon.typechecker "1.2.1";
    import "org.eclipse.ceylon.module-resolver" "1.2.1";
    import org.eclipse.ceylon.compiler.java "1.2.1";
    import org.eclipse.ceylon.compiler.js "1.2.1";
    import org.eclipse.ceylon.model "1.2.1";
}

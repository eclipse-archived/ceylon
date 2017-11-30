"This module should be compiled with a fake 1.2.99 compiler
 and this have dependencies only on fake 1.2.99 dist modules"
native("jvm") module compiled1299 "1.0.0" {
    import ceylon.bootstrap "1.2.99";
    import ceylon.runtime "1.2.99";
    //import ceylon.language "1.2.99";
    import org.eclipse.ceylon.common "1.2.99";
    import org.eclipse.ceylon.typechecker "1.2.99";
    import "org.eclipse.ceylon.module-resolver" "1.2.99";
    import org.eclipse.ceylon.compiler.java "1.2.99";
    import org.eclipse.ceylon.compiler.js "1.2.99";
    import org.eclipse.ceylon.model "1.2.99";
}

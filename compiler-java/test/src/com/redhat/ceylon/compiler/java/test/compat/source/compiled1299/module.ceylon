"This module should be compiled with a fake 1.2.99 compiler
 and this have dependencies only on fake 1.2.99 dist modules"
native("jvm") module compiled1299 "1.0.0" {
    import ceylon.bootstrap "1.2.99";
    import ceylon.runtime "1.2.99";
    //import ceylon.language "1.2.99";
    import com.redhat.ceylon.common "1.2.99";
    import com.redhat.ceylon.typechecker "1.2.99";
    import "com.redhat.ceylon.module-resolver" "1.2.99";
    import com.redhat.ceylon.compiler.java "1.2.99";
    import com.redhat.ceylon.compiler.js "1.2.99";
    import com.redhat.ceylon.model "1.2.99";
}

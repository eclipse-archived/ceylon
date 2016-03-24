"This module should be compiled with the 1.2.0 compiler
 and this have dependencies only on 1.2.0 dist modules"
native("jvm") module compiled120 "1.0.0" {
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

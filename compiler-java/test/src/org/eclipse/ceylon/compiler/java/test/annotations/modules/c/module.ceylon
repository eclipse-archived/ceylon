"Module C doc, with imports!"
module org.eclipse.ceylon.compiler.java.test.annotations.modules.c "0.1" {
    shared import "org.eclipse.ceylon.compiler.java.test.annotations.modules.a" "0.1";
    "Optionally depend on b"
    deprecated("doesn't _do_ anything either") 
    optional import org.eclipse.ceylon.compiler.java.test.annotations.modules.b "0.1";
    
    "module name with a dash"
    import "org.eclipse.ceylon.module-resolver" "1.3.4-SNAPSHOT";
}

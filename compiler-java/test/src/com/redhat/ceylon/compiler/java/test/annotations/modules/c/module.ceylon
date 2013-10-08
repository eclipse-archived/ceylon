"Module C doc, with imports!"
module com.redhat.ceylon.compiler.java.test.annotations.modules.c '0.1' {
    shared import 'com.redhat.ceylon.compiler.java.test.annotations.modules.a' '0.1';
    "Optionally depend on b"
    deprecated("doesn't _do_ anything either") 
    optional import com.redhat.ceylon.compiler.java.test.annotations.modules.b '0.1';
    
    "module name with a dash"
    import 'com.redhat.ceylon.module-resolver' '1.0.0';
}
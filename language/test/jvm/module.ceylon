"Ceylon tests which relate specifically to the JVM version of 
 the runtime. For example because of a dependency on java APIs"
module jvm "0.1" {
    import check "0.1";
    shared import java.base "7";
    import java.desktop "7";
}
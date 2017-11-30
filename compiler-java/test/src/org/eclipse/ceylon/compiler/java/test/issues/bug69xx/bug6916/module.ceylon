native("jvm")
module org.eclipse.ceylon.compiler.java.test.issues.bug69xx.bug6916 "1.0.0" {
    shared import java.base "7";
    shared import javax.servlet "3.1.0";
    shared import ceylon.interop.java "1.3.4-SNAPSHOT";
    
    shared import maven:"org.apache.wicket:wicket-core" "7.1.0";
}
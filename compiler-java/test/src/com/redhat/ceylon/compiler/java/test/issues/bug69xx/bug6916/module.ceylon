native("jvm")
module com.redhat.ceylon.compiler.java.test.issues.bug69xx.bug6916 "1.0.0" {
    shared import java.base "7";
    shared import javax.servlet "3.1.0";
    shared import ceylon.interop.java "1.3.1";
    
    shared import maven:"org.apache.wicket:wicket-core" "7.1.0";
}
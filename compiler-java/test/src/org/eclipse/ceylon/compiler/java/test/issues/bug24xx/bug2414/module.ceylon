native("jvm")
suppressWarnings("ceylonNamespace")
module org.eclipse.ceylon.compiler.java.test.issues.bug24xx.bug2414 "1.0" {
    shared import "org.eclipse.ceylon.module-resolver" "1.2.0";
    
    shared import "org.eclipse.aether:aether-api" "1.0.0.v20140518";
    shared import org.apache.maven:maven-aether-provider" "3.1.0";
                                                                 
    shared import org.eclipse.ceylon.model "1.2.0";
    shared import org.eclipse.ceylon.common "1.2.0";
    shared import java.base "8";
}
import javax.annotation {
    resource,
    Resource {
        AuthenticationType {
            \iAPPLICATION
        }
    }
}

shared class AnnotationInteropQualifiedEnum() {
    
    // 2. case
    shared variable
    resource { authenticationType = Resource.AuthenticationType.\iAPPLICATION; }
    String field1 = "";
    
    // 3. case
    shared variable
    resource { authenticationType = \iAPPLICATION; }
    String field2 = "";
}

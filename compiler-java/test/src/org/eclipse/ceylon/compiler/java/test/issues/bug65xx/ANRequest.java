package org.eclipse.ceylon.compiler.java.test.issues.bug65xx;

class App {
    public static ANRequest.GetRequestBuilder get(String url){ return null; }
}

public class ANRequest/*<T extends ANRequest>*/ {
    
    public static class GetRequestBuilder<T extends GetRequestBuilder> {
    }
}

shared class Transactional(Boolean requiresNew) 
        satisfies MethodAnnotation {
        
    shared Boolean requiresNew = requiresNew;
    
    doc "This method is called whenever Ceylon loads a class with a method
         annotated |transactional|. It registers a transaction management
         interceptor for the method."
    shared actual void onDefineMethod<X,T,P...>(OpenMethod<X,T,P...> method) {
        method.intercept() 
                onInvoke(X instance, T proceed(P... args), P... args) {
            if (currentTransaction.inProcess || !requiresNew) {
                return proceed(args)
            }
            else {
                currentTransaction.begin();
                try {
                    T result = proceed(args);
                    currentTransaction.commit();
                    return result
                }
                catch (Exception e) {
                    currentTransaction.rollback();
                    throw e
                }
            }
        };
    }
    
}

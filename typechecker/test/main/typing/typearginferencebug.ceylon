Integer simpleFunction() => 1;
void logSuccess( Integer result ) => print( "Succeeded: ``result``" );
void logFailure( Exception e ) => print( "Failed: ``e.message``" );


// One parameter in the task works ok ...
void doHigherOrderStuff1<T>( 
    Anything( Anything(T) ) task,
    Anything(T) succeed  
) {
    task( succeed );
}

void makeTask1<T>( T() simpleFunction )( Anything(T) succeed ) {
    succeed( simpleFunction() );
}

void willCompile() {
    doHigherOrderStuff1( 
        makeTask1( simpleFunction ), 
        logSuccess 
    );
}

// Two parameetrs in the task stymies higher order function call ...
void doHigherOrderStuff2<T>( 
    Anything( Anything(T), Anything(Exception) ) task,
    Anything(T) succeed,
    Anything(Exception) fail  
) {
    task( succeed, fail );
}

void makeTask2<T>( T() simpleFunction )( Anything(T) succeed, Anything(Exception) fail ) {
    try {
        T result = simpleFunction();
        succeed( result );
    }
    catch ( Exception e ) {
        fail(e);
    }
}

void willNotCompile() {
    doHigherOrderStuff2( 
        makeTask2( simpleFunction ), 
        logSuccess /* bogus error re: Anything(Integer|Exception) */, 
        logFailure 
    );
}

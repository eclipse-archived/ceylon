
import ceylon.language.meta {
    type
}

@test
shared void bug5966(){
    print( type( `Integer` ) ); // ok
    
    print( type( `Integer|String` ) ); // org.eclipse.ceylon.model.loader.ModelResolutionException
    
    print( type( `Resource&Obtainable` ) ); // org.eclipse.ceylon.model.loader.ModelResolutionException
}
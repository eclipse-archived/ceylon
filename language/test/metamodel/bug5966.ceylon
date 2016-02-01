
import ceylon.language.meta {
    type
}

@test
shared void bug5966(){
    print( type( `Integer` ) ); // ok
    
    print( type( `Integer|String` ) ); // com.redhat.ceylon.model.loader.ModelResolutionException
    
    print( type( `Resource&Obtainable` ) ); // com.redhat.ceylon.model.loader.ModelResolutionException
}
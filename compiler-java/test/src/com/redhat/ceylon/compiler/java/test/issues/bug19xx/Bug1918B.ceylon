shared class Bug1918B(){} 
shared class Bug1918C(){}

shared class Bug1918Dummy() 
{
    List<Bug1918B | Bug1918C|Integer | String> valList = nothing;
    while(exists current = valList.first ){
        
        if(is Bug1918B current) {
        } else if(is Bug1918C|Integer current) { // cannot find symbol
        }
    }
}
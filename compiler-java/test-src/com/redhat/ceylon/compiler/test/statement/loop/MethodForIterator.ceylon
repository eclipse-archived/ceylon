@nomodel
class MethodForIterator(){
    shared void m(Sequence<String> seq){
        for(String s in seq){
            // Empty
        }
    }
    shared void m2(){
        for(String s in {"aap","noot","mies"}){
            // Empty
        }
        for(String? s in {"aap",null,"mies"}){
            // Empty
        }
        for(Natural n in {1,2,3}){
            // Empty
        }
        for(Natural? n in {1,null,3}){
            // Empty
        }
    }
}
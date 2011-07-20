@nomodel
class MethodForFail(){
    shared Natural m(Sequence<Integer> seq){
        for(Integer i in seq){
            while (true) {
                for(Integer j in seq){
                    break;
                }
                else {
                    return 1;
                }
                break;
            }
            break;
        }
        else {
            return 0;
        }
        return 2;
    }
}

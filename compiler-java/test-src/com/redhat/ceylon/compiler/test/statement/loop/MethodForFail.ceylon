@nomodel
class MethodForFail(){
    shared Natural m(Sequence<Integer> seq){
        for(Integer i in seq){
            while (true) {
                for(Integer j in seq){
                    break;
                }
                fail {
                    return 1;
                }
                break;
            }
            do {
                break;
            } while (true);
            break;
        }
        fail {
            return 0;
        }
        return 2;
    }
}

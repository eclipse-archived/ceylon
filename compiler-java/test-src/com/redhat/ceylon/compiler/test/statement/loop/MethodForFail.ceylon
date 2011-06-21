class MethodForFail(){
    shared Natural m(Iterator<Integer> it){
        for(Integer i in it){
            while (true) {
                for(Integer i in it){
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
    }
}

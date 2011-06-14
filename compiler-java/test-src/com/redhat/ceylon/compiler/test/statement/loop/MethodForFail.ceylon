class MethodForIterator(){
    shared Integer m(){
        for(Integer i in {}){
            return 1;
        }
        fail {
            return 0;
        }
    }
}

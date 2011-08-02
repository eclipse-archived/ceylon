@nomodel
class MethodWhile(){
    shared Natural m(){
        variable Boolean cont := true;
        while (cont) {
            cont := false;
        }
        return 0;
    }
}

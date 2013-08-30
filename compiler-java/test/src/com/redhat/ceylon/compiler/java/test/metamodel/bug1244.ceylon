abstract class Bug1244(){
    shared object objectMember {}
}

void bug1244() {
    String s = `value Bug1244.objectMember`.name;
}
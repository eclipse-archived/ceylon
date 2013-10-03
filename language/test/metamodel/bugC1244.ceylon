abstract class BugC1244(){
    shared object objectMember {}
}

@test
shared void bugC1244() {
    String s = `value BugC1244.objectMember`.name;
}
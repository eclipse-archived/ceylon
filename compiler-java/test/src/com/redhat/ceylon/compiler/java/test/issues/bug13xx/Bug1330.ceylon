@noanno
class Bug1330_2() {
    shared class Node() {}
    
    void m(){
        Node [] nodes = [];
        nodes.filter((Node elem) => false);
    }
}
@noanno
class Bug1330<N>() {
    shared class Node() {}

    void m(){
        Node [] nodes = [];
        nodes.filter((Node elem) => false);
    }
}

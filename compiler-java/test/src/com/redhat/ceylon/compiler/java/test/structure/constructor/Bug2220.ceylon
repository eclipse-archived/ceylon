@noanno
shared class Bug2220(){
    shared class Bar(){
        shared class Baz{
            shared new baz{}
            shared new gee(){}
        }
        shared Baz m() => Baz.baz;
        shared Baz n() => Baz.gee();
    }   
}
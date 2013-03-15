@nomodel
class Foo(
            shared String s, 
            shared Boolean b,
            shared Character c, 
            shared Integer i = 5) { }

@nomodel
annotation Foo bar(String s, Character c='h') => Foo(s, true, c);

@nomodel
//doc("Type doc")
//shared 
//abstract 
bar("gee")
class DocAnnotation() {
    
    //"member attr"
    bar("gee", 'z') 
    //shared 
    Integer a = 0;
    
    //"member method"
    //shared default
    bar("gee", 'z') 
    void m() { 
    }
    
}
void lazyeager() {
    class Person(name) { shared String name; }
    [String*] sequence = [];
    {String*} iterable = {};
    Person[] people = [ Person("Gavin") ];
    
    @type:"Empty" 
    value x1 = {};                               //type []
    @type:"Iterable<String,Null>" 
    value x2 = { for (p in people) p.name };     //type {String*}
    @type:"Iterable<String,Nothing>" 
    value x8 = { "hello" };                      //type {String+}
    @type:"Iterable<String,Nothing>" 
    value x3 = { "hello", "world", "goodbye" };  //type {String+}
    @type:"Iterable<String,Nothing>" 
    value x4 = { "hello", *sequence };         //type {String+}
    @type:"Iterable<String,Nothing>" 
    value x5 = { "hello", *iterable };         //type {String+}
    @type:"Iterable<String,Null>" 
    value x6 = { *sequence };                  //type {String*}
    @type:"Iterable<String,Null>" 
    value x7 = { *iterable };                  //type {String*}

    @type:"Empty" 
    value y1 = [];                               //type []
    @type:"Sequential<String>" 
    value y2 = [ for (p in people) p.name ];     //type [String*]
    @type:"Tuple<String,String,Empty>" 
    value y8 = [ "hello" ];                      //type [String]
    @type:"Tuple<String,String,Tuple<String,String,Tuple<String,String,Empty>>>" 
    value y3 = [ "hello", "world", "goodbye" ];  //type [String, String, String]
    @type:"Tuple<String,String,Sequential<String>>" 
    value y4 = [ "hello", *sequence ];         //type [String, String*]
    @type:"Tuple<String,String,Iterable<String,Null>>"
    value y5 = [ @error "hello", *iterable ];         //type [String, String*]
    @type:"Tuple<String,String,Sequential<String>>"
    value y9 = [ "hello", *iterable.sequence ];         //type [String, String*]
    @type:"Sequential<String>" 
    value y6 = [ *sequence ];                  //type [String*]
    @type:"Iterable<String,Null>" 
    value y7 = [ @error *iterable ];                  //error
    @type:"Sequential<String>" 
    value y10 = [ *iterable.sequence ];                  //type [String*]
    
    {Character*} chariter = {*"hello"};
    [Character*] charseq = [*"hello"];
    void f(Character* strings) {
        value val = strings;
    }
    f(*"hello");
    f(*charseq);
    f(` `, *charseq); //TODO!!!
    @error f(*chariter);
    @error f(` `, *chariter);
    value g = f;
    g(*"hello");
    g(*charseq);
    g(` `, *charseq); //TODO!!!
    @error g(*chariter);
    @error g(` `, *chariter);

}
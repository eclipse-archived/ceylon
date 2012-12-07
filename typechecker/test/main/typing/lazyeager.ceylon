void lazyeager() {
    class Person(name) { shared String name; }
    [String...] sequence = [];
    {String...} iterable = {};
    Person[] people = { Person("Gavin") };
    
    @type:"Empty" 
    value x1 = {};                               //type []
    @type:"Iterable<String>" 
    value x2 = { for (p in people) p.name };     //type {String...}
    @type:"Tuple<String,String,Empty>" 
    value x8 = { "hello" };                      //type [String]
    @type:"Tuple<String,String,Tuple<String,String,Tuple<String,String,Empty>>>" 
    value x3 = { "hello", "world", "goodbye" };  //type [String, String, String]
    @type:"Tuple<String,String,Sequential<String>>" 
    value x4 = { "hello", sequence... };         //type [String, String...]
    @type:"Iterable<String>" 
    value x5 = { "hello", iterable... };         //type {String...}
    @type:"Sequential<String>" 
    value x6 = { sequence... };                  //type [String, String...]
    @type:"Iterable<String>" 
    value x7 = { iterable... };                  //type {String...}

    @type:"Empty" 
    value y1 = [];                               //type []
    @type:"Sequential<String>" 
    value y2 = [ for (p in people) p.name ];     //type [String...]
    @type:"Tuple<String,String,Empty>" 
    value y8 = [ "hello" ];                      //type [String]
    @type:"Tuple<String,String,Tuple<String,String,Tuple<String,String,Empty>>>" 
    value y3 = [ "hello", "world", "goodbye" ];  //type [String, String, String]
    @type:"Tuple<String,String,Sequential<String>>" 
    value y4 = [ "hello", sequence... ];         //type [String, String...]
    @type:"Tuple<String,String,Sequential<String>>"
    value y5 = [ "hello", iterable... ];         //type [String, String...]
    @type:"Sequential<String>" 
    value y6 = [ sequence... ];                  //type [String, String...]
    @type:"Sequential<String>" 
    value y7 = [ iterable... ];                  //type [String, String...]
    
    {Character...} sequential = {"hello"...};
    void f(Character... strings) {
        value val = strings;
    }
    f("hello"...);
    f(sequential...);
    @error f(` `, sequential...); //TODO!!!
    value g = f;
    g("hello"...);
    @error g(` `, sequential...); //TODO!!!

}
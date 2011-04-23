public class Literals() {
    
    @type["String"] local s = "Hello";
    @type["Natural"] local n = 1;
    @type["Float"] local f = 1.0;
    @type["Character"] local c = `x`;
    @type["Sequence<String>"] local ss = { "hello", "world" };
    @type["Sequence<Natural>"] local ns = { 1, 2, 3, 4 };
    @type["String"] local st = "pi = " 3.1415 " approx";
    @type["Quoted"] local q = 'hibernate.org';
    
    Boolean b = true;
    
    String[] strings = { "Hello", "World" };
    
    String? maybeString = null;
    
    Iterable<String> istrings = strings;
    
    @type["Nothing|Entry<String,String>"] local sw = process.switches[3];
    
}
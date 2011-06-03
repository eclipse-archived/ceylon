public class Literals() {
    
    @type["String"] local s = "Hello";
    @type["Natural"] local n = 1;
    @type["Integer"] local im = -1;
    @type["Integer"] local ip = +1;
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
    
    @type["Nothing|String"] local sw = process.switches["file"];
    
    @type["Float"] local ff = 1.float;
    @type["Integer"] local ii = 1.0.integer;
    @type["Boolean"] local bb = 1.0.positive;
    
    @type["Natural"] local nnn = 2.minus(1);
    @type["Float"] local fff = 2.0.times(-3.0);
    
}
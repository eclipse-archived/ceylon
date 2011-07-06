public class Literals() {
    
    @type["String"] value s = "Hello";
    @type["Natural"] value n = 1;
    @type["Integer"] value im = -1;
    @type["Integer"] value ip = +1;
    @type["Float"] value f = 1.0;
    @type["Character"] value c = `x`;
    @type["Sequence<String>"] value ss = { "hello", "world" };
    @type["Sequence<Natural>"] value ns = { 1, 2, 3, 4 };
    @type["String"] value st = "pi = " 3.1415 " approx";
    @type["Quoted"] value q = 'hibernate.org';
    
    Boolean b = true;
    
    String[] strings = { "Hello", "World" };
    
    String? maybeString = null;
    
    Iterable<String> istrings = strings;
    
    @type["Nothing|String"] value sw = process.switches["file"];
    
    @type["Float"] value ff = 1.float;
    @type["Integer"] value ii = 1.0.integer;
    @type["Boolean"] value bb = 1.0.positive;
    @type["Integer"] value iii = 3.positiveValue;
    
    @type["Natural"] value nnn = 2.minus(1);
    @type["Float"] value fff = 2.0.times(-3.0);
    
}
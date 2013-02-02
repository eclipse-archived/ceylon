class Literals() {
    
    @type:"String" value s = "Hello";
    @type:"Integer" value n = 1;
    @type:"Integer" value nsi = 1k;
    @type:"Integer" value ng = 1_000_000;
    @type:"Integer" value im = -1;
    @type:"Integer" value ip = +1;
    @type:"Float" value f = 1.0;
    @type:"Float" value fe1 = 2.4e12;
    @type:"Float" value fe2 = 12.437E-9;
    @type:"Float" value fsi1 = 12u;
    @type:"Float" value fsi2 = 3.56M;
    @type:"Character" value c = 'x';
    @type:"Tuple<String,String,Tuple<String,String,Empty>>" 
    value ss = [ "hello", "world" ];
    @type:"Tuple<Integer,Integer,Tuple<Integer,Integer,Tuple<Integer,Integer,Tuple<Integer,Integer,Empty>>>>" 
    value ns = [ 1, 2, 3, 4 ];
    @type:"Tuple<String,String,Tuple<String,String,Empty>>" 
    value ss2 = [ "hello", "world" ];
    @type:"Tuple<Integer,Integer,Tuple<Integer,Integer,Tuple<Integer,Integer,Tuple<Integer,Integer,Empty>>>>" 
    value ns2 = [ 1, 2, 3, 4 ];
    @type:"String" value st1 = "pi = ``3.1415`` approx";
    @type:"String" value st2 = "pi = ```3.1415``` approx";
    @type:"String" value st3 = "pi = '``3.1415``' approx";
    @type:"String" value st4 = "pi = \"``3.1415``\" approx";
    @type:"String" value st5 = "x = ``1``, y = ``0``";
    @type:"String" value st6 = "x = ``"hello"``, y = ``"goodbye"``";
    @type:"String" value q = "hibernate.org";
    @type:"String" value vs1 = """Some punctuation characters: "'`\""";
    @type:"String" value vs2 = """"Hello!"""";
    @type:"String" value vs3 = """The empty string is written """"";
    
    Boolean b = true;
    
    String[] strings = [ "Hello", "World" ];
    
    String? maybeString = null;
    
    Iterable<String> istrings = strings;
    
    //@type:"Null|String" value sw = process.switches["file";
    
    @type:"Float" value ff = 1.float;
    @type:"Integer" value ii = 1.0.integer;
    @type:"Boolean" value bb = 1.0.positive;
    @type:"Integer" value iii = 3.positiveValue;
    
    @type:"Integer" value nnn = 2.minus(1);
    @type:"Float" value fff = 2.0.times(-3.0);
    
    @type:"String" @error value st2 = "( ``null`` )";
    
    @type:"Integer" value hex1 = #FF12;
    @type:"Integer" value hex2 = #FF12_A0E9;
    @type:"Integer" value bin1 = $0101001101;
    @type:"Integer" value bin2 = $0101_0011;
    
}
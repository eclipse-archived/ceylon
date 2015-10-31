class Parsing() {

String foo() => "bar";
String bar(String string) => string;
String baz(String string, Integer integer) 
        => string.repeat(integer);
String qux()(Float float)(String string, Integer integer) => "";

String string = "foo";
Integer integer = 0;
Float float = 1.0;

String x = (foo)();
String y = (bar)(string);
String z = (baz)(string,integer);
String w = (qux)()(float)(string,integer);

Integer()() a = ()() => 1;
String(String)() b = ()(string) { String string; return string; };
String(String,Integer)() c = ()(string,integer) { String string; Integer integer; return string.repeat(integer); };
String(String)()() d = ()()(string) { String string; return string; };
String(String)(Integer)() e = ()(Integer integer)(string) { String string; return string.repeat(integer); };
@error String(String)(Integer)() f = ()(integer)(string) { Integer integer; String string; return string.repeat(integer); };

}
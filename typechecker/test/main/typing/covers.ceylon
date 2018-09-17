interface I<out X, out Y> {}
interface S<out X> of E|T<X> satisfies I<X,Null>{}
interface T<out X> satisfies S<X>&I<X,Nothing> {}
abstract class E() of e satisfies S<Nothing> {}
object e extends E() {}

void coverage() {
    
    I<Nothing,Null>&I<String,Nothing> x1 = nothing;
    I<Nothing,Nothing> y1 = x1 of I<Nothing,Nothing>;
    E&I<String,Nothing> x2 = nothing;
    I<Nothing,Nothing> y2 = x2 of I<Nothing,Nothing>;
    
    S<String>&I<String,Nothing> z = nothing;
    T<String>|I<Nothing,Nothing> u = z of T<String>|I<Nothing,Nothing>;
    S<String> w = z of S<String>;
    T<String> v = z of T<String>;
    $error T<String> t = z;

    {Nothing*}&{String+} something = nothing;
    {Nothing+} it = something;
    
    String[]&{String+} strings = [""];
    []&{String+}|[String+]&{String+} iter0 = strings of []&{String+}|[String+]&{String+};
    []&{String+}|[String+] iter5 = strings of []&{String+}|[String+];
    {Nothing+}|[String+] iter3 = strings of {Nothing+}|[String+];
    [String+]|{Nothing+} iter1 = strings of [String+]|{Nothing+};    
    [String*] iter6 = strings of [String*];
    [String+] iter2 = strings of [String+];
    $error [String+] iter7 = strings;
    
    []&{String+} x0 = nothing;
    {Nothing+} y0 = x0 of {Nothing+};
    {Nothing+}&[] y8 = x0 of {Nothing+}&[];
    Nothing y9 = x0 of Nothing;
    $error []&{String+} x9 = y0;

    {Nothing*}&{String+} x3 = nothing;
    {Nothing+} y3 = x3 of {Nothing+};
    
    $error abstract class Problem() satisfies Empty&{Nothing+} {}
    
    Set<Anything> s1 = nothing;
    Set<Object> s2 = s1 of Set<Object>;
}

T&<Object?> covFun0<T>(T maybe) => maybe of T&<Object?>;

<T&Object>? covFun1<T>(T? maybe) => maybe of <T&Object>?;

void coverageAndConstraints1<U,V>() 
        given U of String|Character 
        given V of Integer|Float {
    U&V whatever = nothing;
    Nothing n = whatever of Nothing;
}

void coverageAndConstraints2<T>() 
        given T of Integer|Float {
    Nothing s = nothing of T & String;
    Nothing t = nothing of String & T;
    void bar<U>() given U satisfies T {
        Nothing s = nothing of U & String;
        Nothing t = nothing of String & U;
    }
}

void useSiteVarianceCoverage() {
    interface I<T> of T {}
    
    void run(I<String> s) {
        I<out Anything> s1 = s; // ok
        I<in Nothing> s2 = s;   // ok
        
        value s11 = s of I<out Anything>; // error 1
        value s22 = s of I<in Nothing>;   // error 2
    }
}

import java.util { Comparator }

@noanno
shared void bug2310(Bug2310Java<Anything> x){
    @type:"Bug2310Java<String>"
    value vt = Bug2310Java(nothing of Comparator<String>);

    @type:"Bug2310Java<String>"
    value vm = x.m(nothing of Comparator<String>);
}
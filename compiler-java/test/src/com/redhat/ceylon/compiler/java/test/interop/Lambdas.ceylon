import java.util.\ifunction {
    Consumer, IntConsumer
}
import java.util {
    ArrayList
}
import java.lang { CharSequence, ShortArray }

void toplevel(Integer i) => print(i);
void toplevelSmall(small Integer i) => print(i);

void lambdas() {
    value j = LambdasJava();
    j.consumer((Boolean b) => print(b), true);
    j.\ifunction((Boolean b) => b, true);
    function f(Integer i) => print(i);
    j.intConsumer(f);
    value fval = (Integer i) => print(i);
    j.intConsumer(fval);
    fval(1);
    value refToIntMethod = LambdasJava.takeInt;
    // make sure we don't wrap this
    IntConsumer fvalNothing = nothing;
    IntConsumer fval2 = (Integer i) => print(i);
    //j.intConsumer(fval2);
    Callable<Anything,[Integer]> consumer = fval2;
    
    String s1 = j.str;
    CharSequence cs1 = j.str;
    String s2 = j.charSequence;
    CharSequence cs2 = j.charSequence;
    j.str = s1;
    j.str = cs1;
    j.charSequence = s2;
    j.charSequence = cs2;

    j.intConsumer(toplevel);
    // FIXME:
    //j.intConsumer(toplevelSmall);
    
    j.intConsumer((Integer i) => print(i));
    j.intSupplier(() => 1);

    value l = ArrayList<Integer>();
    value s = l.stream().filter((Integer i) => i.positive)
            .mapToInt((Integer i) => i)
            .sum();
}

//class Sub(IntConsumer c) extends LambdasJava(c){}

void underlyingTypeTest() {
    value x = `ShortArray`;
    value s = 2;
    value r = ShortArray(s);
}
import java.util.\ifunction {
    Consumer, IntConsumer
}
import java.util {
    ArrayList
}

void toplevel(Integer i) => print(i);

void lambdas() {
    value j = LambdasJava();
    j.consumer((Boolean b) => print(b), true);
    j.\ifunction((Boolean b) => b, true);
    function f(Integer i) => print(i);
    j.intConsumer(f);
    value fval = (Integer i) => print(i);
    j.intConsumer(fval);
    fval(1);

    j.intConsumer(toplevel);
    
    j.intConsumer((Integer i) => print(i));
    j.intSupplier(() => 1);

    value l = ArrayList<Integer>();
    value s = l.stream().filter((Integer i) => i.positive)
            .mapToInt((Integer i) => i)
            .sum();
}

//class Sub(IntConsumer c) extends LambdasJava(c){}

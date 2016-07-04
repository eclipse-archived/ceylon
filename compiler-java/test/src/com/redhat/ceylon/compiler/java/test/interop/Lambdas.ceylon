import java.util.\ifunction {
    Consumer
}
import java.util {
    ArrayList
}

void lambdas() {
    value j = LambdasJava();
    j.consumer((Boolean b) => print(b), true);
    j.\ifunction((Boolean b) => b, true);
    j.intConsumer((Integer i) => print(i));
    j.intSupplier(() => 1);

    value l = ArrayList<Integer>();
    value s = l.stream().filter((Integer i) => i.positive)
            .mapToInt((Integer i) => i)
            .sum();
}

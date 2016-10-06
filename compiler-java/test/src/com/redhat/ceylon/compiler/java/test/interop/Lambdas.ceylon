import java.util.\ifunction {
    Consumer, IntConsumer
}
import java.util {
    ArrayList
}
import java.lang { CharSequence, ShortArray, FloatArray }
import com.redhat.ceylon.compiler.java.test.interop { LambdasJava { consumerStatic }}

void toplevel(Integer i) => print(i);
void toplevelSmall(small Integer i) => print(i);
class C(Integer i){}
class CSmall(small Integer i, small Float v=0.0){}

void lambdas() {
    /* 
     * TODO:
     * - overloaded coercion fields (disallowed for now?)
     * - variadic coercion methods
     * - variadic SAM methods
     * - defaulted params can't happen in theory due to the java limitation
     * - java limitation and ceylon inheritance
     * - make sure typechecker rejects implementing coercion methods/attributes
     * - support named invocations (for auto-factory methods)
     * - ambiguous callable reference to overloaded method
     * Error due to small parameter:
     *     Anything(Integer) ctorRef = FloatArray;
     */ 
    
    value j = LambdasJava();
    j.consumer((Boolean b) => print(b), true);
    j.consumerStatic((Boolean b) => print(b), true);
    consumerStatic((Boolean b) => print(b), true);
    j.\ifunction((Boolean b) => b, true);
    function f(Integer i) => print(i);
    j.intConsumer(f);
    value fval = (Integer i) => print(i);
    j.intConsumer(fval);
    fval(1);
    value refToIntMethod = LambdasJava.takeInt;
    // make sure we don't wrap this
    IntConsumer fvalNothing = nothing;
    j.intConsumer(fvalNothing);

    j.overloadedFunction((Integer i) => print("yes"));
    j.overloadedFunction2((Integer i) => print("yes"));


    LambdasJava(f);

    // Not allowed anymore
//    IntConsumer fval2 = (Integer i) => print(i);
//    j.intConsumer(fval2);
//    Callable<Anything,[Integer]> consumer = fval2;
//    
    String s1 = j.str;
    // Not allowed anymore
    //CharSequence cs1 = j.str;
    String s2 = j.charSequence.string;
    CharSequence cs2 = j.charSequence;
    j.str = s1;
    j.str = cs2.string;
    j.setCharSequence(cs2);
    j.setCharSequence(s2);
    j.charSequence = cs2;
    
    j.intConsumer(toplevel);
    j.intConsumer(toplevelSmall);
    
    j.intConsumer((Integer i) => print(i));
    j.intSupplier(() => 1);

    value tlref = toplevel;
    value tlrefSmall = toplevelSmall;
    value cref = C;
    value crefSmall = CSmall;
    value arrayRef = FloatArray;

    value l = ArrayList<Integer>();
    value s = l.stream().filter((Integer i) => i.positive)
            .mapToInt((Integer i) => i)
            .sum();
}

class Sub(IntConsumer c) extends LambdasJava(c){}

void underlyingTypeTest() {
    value x = `ShortArray`;
    value s = 2;
    value r = ShortArray(s);
}
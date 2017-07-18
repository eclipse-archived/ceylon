import java.util.\ifunction {
    Consumer, IntConsumer, IntSupplier, Function
}
import java.util.stream {
    LongStream, Stream
}
import java.util {
    ArrayList
}
import java.util.concurrent {
    CompletableFuture, ExecutorService
}
import java.lang { CharSequence, ShortArray, FloatArray, LongArray }
import com.redhat.ceylon.compiler.java.test.interop { LambdasJava { consumerStatic }}

void toplevel(Integer i) => print(i);
void toplevelSmall(small Integer i) => print(i);
Integer toplevelVariadic(Integer* i) => 1;
class C(Integer i){}
class CSmall(small Integer i, small Float v=0.0){}

void lambdas() {
    /* 
     * TODO:
     * - status of null in SAM Callable types
     * - limited to java but how does it play with ceylon subtypes?
     * - fix error message when implementing coercion method
     * - review additions to model and their names
     * LATER:
     * - optimise to get rid of call/callvariadic/calltyped methods (but complex wrt default values/variadic mismatch with subtyping)
     * - overloaded coercion fields (disallowed for now?)
     * - support named invocations (for auto-factory methods)
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
    j.intConsumer(Integer);
    j.intConsumer(LambdasJava.takeInt);
    fval(1);
    value refToIntMethod = LambdasJava.takeInt;
    // make sure we don't wrap this
    IntConsumer fvalNothing = nothing;
    j.intConsumer(fvalNothing);

    j.overloadedFunction((Integer i) => print("yes"));
    j.overloadedFunction2((Integer i) => print("yes"));

    j.variadicFunction((Integer* args) => print("ok"));
    j.intSupplier((Integer* args) => 1);
    j.variadicFunction(toplevelVariadic);
    j.intSupplier(toplevelVariadic);

    j.consumeTwoIntegers((Integer a, Integer b) => a+b);
    value f1 = uncurry(Integer.plus);
    function f2() => uncurry(Integer.plus);
    j.consumeTwoIntegers(f1);
    j.consumeTwoIntegers(f2());

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

    j.charSequences();
    j.charSequences("a");
    j.charSequences("a", "b");
    
    //
    // Arrays

    j.arrays(Array{1, 2}, Array{1.0, 2.0}, Array{1.byte, 2.byte}, Array{true, false}, Array{j, null}, Array{j});

    
    class LocalClass(){}

    // use constants
    j.setKlass(`LambdasJava`);
    j.setKlass(`LocalClass`);
    j.setKlass(`LambdasJava.StaticMemberClass`);
    j.setKlass(`LambdasJava.MemberClass`);
    j.klassMethod(`LambdasJava`);
    value jklass1 = j.klassMethodParameterised(`LambdasJava`);
    value jklass2 = j.klassMethodParameterisedCovariant(`LambdasJava`);
    value jklass3 = j.klassMethodParameterisedWithBounds(`LambdasJava`);
    value klass = `LambdasJava`;
    // use Util function
    j.setKlass(klass);
    j.klassMethod(klass);

    // make sure overloading prefers non-coerced methods in doubt
    j.intConsumer(null);
    LambdasJava(null);
    
    j.intConsumer(toplevel);
    j.intConsumer(toplevelSmall);
    
    j.intConsumer((Integer i) => print(i));
    j.intConsumer((Integer i=2, Integer j=3) => print(i));
    j.intSupplier(() => 1);
    j.intSuppliers();
    j.intSuppliers(() => 1);
    j.intSuppliers(() => 1, () => 2);

    value tlref = toplevel;
    value tlrefSmall = toplevelSmall;
    value cref = C;
    value crefSmall = CSmall;
    value arrayRef = FloatArray;

    value l = ArrayList<Integer>();
    value s = l.stream().filter((Integer i) => i.positive)
            .mapToInt((Integer i) => i)
            .sum();

    ExecutorService mes = nothing;
    
    CompletableFuture.supplyAsync(() => 2, mes.execute);
    CompletableFuture.supplyAsync(() => 2);

    CompletableFuture.supplyAsync(()=> "Hello")
            .thenApply(String.uppercased)
            .thenAccept(print);
    
    j.stringFunction((String s) => s);
    j.stringFunction((String? s) => s);
    j.stringFunction((String s) => (s of String?));

    String capturedString = "";
    j.stringFunction2((String s) => capturedString);
    String? capturedString2 = "";
    j.stringFunction2((String s) => capturedString2);
    
    // make sure Iterable is special-cased and not treated as a SAM
    @type:"Anything(Iterable<String>?)"
    value xf = j.takeIterableString;

    // make sure Annotation is special-cased and not treated as a SAM
    @type:"Anything(Annotation?)"
    value xf2 = j.takeAnnotation;
}

@noanno
shared void bug6634() {
    LongStream longStream => LongStream.\iof(1,2,3);
    longStream.map((Integer i) => i).forEach(print); // ok
    longStream.map((Integer? i) => i else 0).forEach(print); // ok
    
    Stream<Integer> integerStream => Stream.\iof(1,null,3);
    integerStream.map((Integer? i) => i else 0).forEach(print); // ok
    integerStream.map((Integer? i) => i).forEach(print);
    integerStream.map((Integer? i) => null).forEach(print); // error
    value f = object satisfies Function<Integer?, Nothing> {
        apply(Integer? i) => null;
    };
    
    integerStream.map((Integer? i) => null of Integer?).forEach(print); // ok
    try {
        integerStream.map((Integer i) => i).forEach(print); // ok
    }
    catch (AssertionError e) {
        print("good"); // ok
    }

    // errors last because they stop code generation in the block
    @error
    longStream.map((Integer? i) => i).forEach(print); // error
    
    @error
    longStream.map((Integer? i) => null).forEach(print); // error
}

class Sub(IntConsumer c) extends LambdasJava(c){
    shared void runWithLock(void action()) => synchronizedRun(action);
}

class Sub2() satisfies InterfaceWithCoercedMembers {
    shared actual void m(CharSequence cs, IntSupplier l){}
}

void underlyingTypeTest() {
    value x = `ShortArray`;
    value s = 2;
    value r = ShortArray(s);
}

import com.ceylon.java8 { Interface { staticMethod }, ... }
import java.time { ... }
import java.util { List }
import java.util.\ifunction { Predicate, ToIntFunction }

class Impl() satisfies Interface {}

void foo(Test t, Interface i, List<Integer> l){
    //t.methodWithParameterNames{first = "a"; second = "b";};
    t.methodWithParameterNames("a", "b");
    t.methodWithTypeAnnotation(null);
    staticMethod();
    i.defaultMethod();
    
    object pred satisfies Predicate<Integer>{
        shared actual Boolean test(Integer i) => i > 2;
    }
    object map satisfies ToIntFunction<Integer>{
        shared actual Integer applyAsInt(Integer i) => i * 3;
    }
    Integer res = l.stream().filter(pred).mapToInt(map).sum();
}

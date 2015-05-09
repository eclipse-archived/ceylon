interface InvariantSupertype {
    
    interface List<T> {
        shared formal Iterator<T> iterator();
        shared formal T? get(Integer index);
        shared formal T? first;
        shared formal void add(T element);
    }
    
    interface Array<T> satisfies List<T> {}
    
    void testInheritance<X>(X x, Array<out X> outArray, Array<in X> inArray) {
        Iterator<X> outIt = outArray.iterator();
        @error Iterator<X> inIt = inArray.iterator();
        X? outFirst = outArray.first;
        @error X? inFirst = inArray.first;
        X? outGet = outArray.get(0);
        @error X? inGet = inArray.get(0);
        inArray.add(x);
        @error outArray.add(x);
    }
    
}


interface CovariantSupertype {
    
    interface List<out T> {
        shared formal Iterator<T> iterator();
        shared formal T? get(Integer index);
        shared formal T? first;
    }
    
    interface Array<T> satisfies List<T> {
        shared formal void add(T element);
    }
    
    void testInheritance<X>(X x, Array<out X> outArray, Array<in X> inArray) {
        Iterator<X> outIt = outArray.iterator();
        @error Iterator<X> inIt = inArray.iterator();
        X? outFirst = outArray.first;
        @error X? inFirst = inArray.first;
        X? outGet = outArray.get(0);
        @error X? inGet = inArray.get(0);
        inArray.add(x);
        @error outArray.add(x);
    }
    
}

class InvariantClass<T>() {}
interface InvariantInterface<T> {
    shared formal T get();
    shared formal void set(T t);
}
@error class ExtendsOut() extends InvariantClass<out Float>() {}
@error abstract class SatisfiesOut() satisfies InvariantInterface<out Float> {}
@error class ExtendsIn() extends InvariantClass<in Float>() {}
@error abstract class SatisfiesIn() satisfies InvariantInterface<in Float> {}
class WithOutTypeParameter<T>(T t) 
@error given T satisfies InvariantInterface<out T>{
    T tt = t.get();
    @error t.set(t);
}
class WithInTypeParameter<T>(T t) 
@error given T satisfies InvariantInterface<in T>{
    @error T tt = t.get();
    t.set(t);
}
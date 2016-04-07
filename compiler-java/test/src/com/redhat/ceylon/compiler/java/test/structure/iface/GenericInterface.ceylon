interface GenericInterface<T> {
    shared T produce() => nothing;
    shared void consume(T t) {
        print(t);
    }
    shared void f(T t, T[] ts = [t]) {}
}
interface GenericInterface2<Y> satisfies GenericInterface<Y> {
    
}
interface GenericInterface3 satisfies GenericInterface<String> {
    
}
class GenericInterfaceSatisfier<X>() satisfies GenericInterface2<String>&GenericInterface3 {
    
}
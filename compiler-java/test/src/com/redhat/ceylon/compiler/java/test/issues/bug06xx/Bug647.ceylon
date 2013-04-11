@nomodel
void bug647<T>(T x) {
}
@nomodel
Array<Element> arrayOfSome<Element>(Element* elements) => nothing;
@nomodel
void bug647test() {
    bug647("foo");
    bug647<Object>("foo");
    bug647{
        x="foo";
    };
    bug647<Object>{
        x="foo";
    };
    
    Array<Object> objs = arrayOfSome<Object>("one", "two", "three");
}
Float addInteger(Float x, Integer y) => x+y;
void printAll(String sep, String* strings) => print(sep.join(*strings));

void tupleargs() {
    value onetwo = [1.0, 2];
    Float three1 = addInteger(*onetwo);
    Float three0 = addInteger(0.0, *[3]);
    value add = addInteger;
    Float three2 = add(*onetwo);
    Float three3 = add(0.0, *[3]);
    value iter = {1.0, 2};
    @error addInteger(*iter);
    @error add(*iter);
    value wrong = [1.0, 2.0];
    @error addInteger(*wrong);
    @error add(*wrong);
    printAll(*[","]);
    printAll(*[",", "hello", "world"]);
    @error printAll(*[",", "hello", 1]);
} 
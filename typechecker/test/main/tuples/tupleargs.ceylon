Float addInteger(Float x, Integer y) => x+y;
void printAll(String sep, String* strings) => print(sep.join(*strings));
X|Y oneOf<X,Y>(X x, Y y) => x;
T? firstOf<T>(T* rest) => rest.first;

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
    value tooLong = [1.0, 2, 2];
    @error addInteger(*tooLong);
    @error add(*tooLong);
    value tooShort = [1.0];
    @error addInteger(*tooShort);
    @error add(*tooShort);
    printAll(*[","]);
    printAll(*[",", "hello", "world"]);
    @error printAll(*[",", "hello", 1]);
    @type:"Integer|Float" oneOf(*[1, 1.0]);
    @type:"String" oneOf(*["hello", "world"]);
    @type:"Integer|Float" oneOf(1, *[1.0]);
    @error @type:"Integer|Float" oneOf(*[1, 1, 1.0]);
    @type:"Null|Integer|Float|String|Character" firstOf(1,2.0,*["hello", 'W']);
}

Any pipe<Any>(Any anything) => anything;
<Any> => Any(Any) pipeRef = pipe;//generic reference
Number add<Number>(Number x, Number y)
        given Number satisfies Summable<Number>
        => x+y;
<Number> given Number satisfies Summable<Number> 
        => Number(Number,Number) addRef = add;//generic reference

<First, Second> given First satisfies Object
        => Entry<First,Second>(First,Second) pairRef = Entry;//generic class reference
<First, Second> 
        given First satisfies Object
        given Second  satisfies Object
        => Entry<First,Second>(First,Second) pairRef2 = pairRef;//generic class reference

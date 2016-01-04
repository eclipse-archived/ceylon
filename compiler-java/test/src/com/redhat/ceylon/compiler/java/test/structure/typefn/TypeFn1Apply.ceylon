@noanno
void genericFunctionReference() {
    String(String) stringPipeRef = pipeRef<String>;//apply
    assert("" == stringPipeRef(""));//call
    assert("hello" == pipeRef("hello"));//apply and call in one
}

@noanno
void genericLocalFunctionReference() {
    Float(Float,Float) floatAddRef = addRef<Float>;//apply
    assert(2.0 == floatAddRef(1.0, 1.0));//call
    String(String,String) stringAddRef = addRef<String>;//apply
    assert("hello world" == stringAddRef("hello", " world"));//call
    assert("hello, world" == addRef("hello,", " world"));//apply and call
}

@noanno
void genericClassReference() {
    Entry<String,String>(String,String) stringPair = pairRef<String,String>;//apply
    assert("hello->world" == stringPair("hello", "world").string);//call
    assert("hello->world" == pairRef("hello", "world").string);//apply and call
}
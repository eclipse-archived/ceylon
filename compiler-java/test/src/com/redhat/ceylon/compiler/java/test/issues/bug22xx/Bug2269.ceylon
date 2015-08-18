interface I2269<Param> {}

I2269<Param> my2269<Param>() 
        => object satisfies I2269<Param> {};

void f2269<Param>() {
    switch(bundle = my2269<Param>())
    case(is I2269<Param>) {}
}

shared void bug2269() {
    f2269<String>();
}
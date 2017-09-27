class MissingConstructorBody {
    shared new init(Foo? o){}
     shared new(void append(Integer i)) extends init(null) {}
}
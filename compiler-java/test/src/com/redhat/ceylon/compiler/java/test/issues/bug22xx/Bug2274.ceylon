shared class Bug2274{
    @error
    shared foo new init() {}
    shared new () extends init(){} 
}
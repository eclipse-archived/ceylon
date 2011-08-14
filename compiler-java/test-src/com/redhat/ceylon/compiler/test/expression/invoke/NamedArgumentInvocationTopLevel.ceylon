@nomodel
shared Natural f(Natural n, String s) {
    return n; 
}

@nomodel
shared void v(Natural n, String s) {
}

@nomodel
Natural x = f{
    s="abc";
    n=123;
};
@nomodel
Void y = v{
    s="abc";
    n=123;
};
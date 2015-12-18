
{T+} typeFnReifiedIsPlus<T>(T t) => [t];
{T*} typeFnReifiedIsStar<T>(T t) => [t];
shared void typeFnReifiedIs() {
    <Arg> => {Arg*}(Arg) ref = typeFnReifiedIsPlus;
    assert(is <S> => {S+}(S) ref);
    {String+} s = ref<String>("hello");
    assert(s.first == "hello");
    <Arg> => {Arg*}(Arg) ref2 = typeFnReifiedIsStar;
    assert(!is <S> => {S+}(S) ref2);
}
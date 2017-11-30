import ceylon.language.meta { type }
import ceylon.language.meta.model { Function, IncompatibleTypeException }

shared [K+] bug675Sequence<K>(K+ ret) => ret;
shared {K+} bug675Iterable<K>({K+} ret) => ret;

shared void bug675Tester([String *] k) => print(k);

[K+] bug675Seq<K>(K+ ret) {
    print("-- " + type(ret).string);
    assert((ret of Anything) is [K+]);
    return ret;
}

@test
shared void bug675() {
    [String+] k = [ "hi", "hello" ];
    value l = bug675Sequence<Object?>( "hi", "hello" );
    value s = `bug675Sequence<String>`;
    value i = `bug675Iterable<String>`;
    
    print(s.type); // ceylon.language::Sequence<ceylon.language::String>
    print(i.type); // ceylon.language::Iterable<ceylon.language::String>
    // each element passed to bug675Sequence is String, so type checks work
    Object kseq = s.apply(*k);
    // This works, because each element is a String, but it wraps the given sequence to get a variadic sequential of the right type
    Object lseq = s.apply(*l);
    try{
        // this one actually throws because it checks the parameter type
        Object lseq2 = s.namedApply{"ret" -> l};
        assert(false);
    }catch(IncompatibleTypeException x){}
    try{
        // this one actually throws because it checks the parameter type
        Object lit2 = i.namedApply{"ret" -> l};
        assert(false);
    }catch(IncompatibleTypeException x){}
    assert(is [String+] kseq);
    assert(is [String+] lseq);
    print(type(kseq)); // ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::Tuple<ceylon.language::String,ceylon.language::String,ceylon.language::empty>>
    print(type(lseq)); // ceylon.language::ArraySequence<ceylon.language::Null|ceylon.language::Object>
    bug675Tester(kseq); // [hi, hello]
    bug675Tester(lseq); // [hi, hello]
    `bug675Tester`.apply(kseq); // [hi, hello]
    `bug675Tester`.apply(lseq); // ******** CRASH *********
    
    // second test
    [<Integer|String>*] l2 = { 1, "hi", "hello" }.sequence().rest;
    value s2 = `bug675Seq<String>`;
    s2.apply(*l2);
}

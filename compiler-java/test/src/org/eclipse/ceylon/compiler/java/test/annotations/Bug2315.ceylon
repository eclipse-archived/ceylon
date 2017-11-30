import ceylon.language.meta.declaration{ClassDeclaration}

@nomodel
final annotation class Bug2315([String+] strings) 
    satisfies OptionalAnnotation<Bug2315, ClassDeclaration>{}
@nomodel
annotation Bug2315 bug2315([String+] strings) => Bug2315(strings);

@nomodel
final annotation class Bug2315Integer([Integer+] ints) 
        satisfies OptionalAnnotation<Bug2315Integer, ClassDeclaration>{}
@nomodel
annotation Bug2315Integer bug2315Integer([Integer+] ints) => Bug2315Integer(ints);
@nomodel
final annotation class Bug2315Class([ClassDeclaration+] classes) 
        satisfies OptionalAnnotation<Bug2315Class, ClassDeclaration>{}
@nomodel
annotation Bug2315Class bug2315Class([ClassDeclaration+] classes) => Bug2315Class(classes);

@nomodel
bug2315Iterable(["foo", "bar"])
final annotation class Bug2315Iterable({String+} strings) 
        satisfies SequencedAnnotation<Bug2315Iterable, ClassDeclaration>{}
@nomodel
annotation Bug2315Iterable bug2315Iterable({String+} strings) => Bug2315Iterable(strings);
annotation Bug2315Iterable bug2315Iterable2([String+] strings) => Bug2315Iterable(strings);

@nomodel
bug2315(["foo"])
bug2315Integer([1])
bug2315Class([`class Bug2315Use`])
bug2315Iterable({"foo"})
bug2315Iterable2(["foo"])
class Bug2315Use() {}
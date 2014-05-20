import ceylon.language.meta.model { Attribute, Method, MemberClass, MemberInterface }

final annotation class Bug259_Annotation1() satisfies OptionalAnnotation<Bug259_Annotation1> {}
annotation Bug259_Annotation1 bug259_Annotation1() => Bug259_Annotation1();

final annotation class Bug259_Annotation2() satisfies OptionalAnnotation<Bug259_Annotation2> {}
annotation Bug259_Annotation2 bug259_Annotation2() => Bug259_Annotation2();

class Bug259(){
    shared default bug259_Annotation1 Integer attribute1 => 1;
    shared Integer attribute2 => 1;
    bug259_Annotation1 Integer attribute3 => 1;

    shared default bug259_Annotation1 Integer method1() => 1;
    shared Integer method2() => 1;
    bug259_Annotation1 Integer method3() => 1;

    shared variable bug259_Annotation1 String attribute4 = "";
    shared variable String attribute5 = "";
    variable bug259_Annotation1 String attribute6 = "";

    shared default bug259_Annotation1 class Class1(){}
    shared class Class2(){}
    bug259_Annotation1 class Class3(){}

    shared bug259_Annotation1 interface Interface1{}
    shared interface Interface2{}
    bug259_Annotation1 interface Interface3{}
}

class Bug259Sub() extends Bug259(){
    shared actual bug259_Annotation1 Integer attribute1 => 1;
    shared Integer attribute2Sub => 1;

    shared actual bug259_Annotation1 Integer method1() => 1;
    shared Integer method2Sub() => 1;

    shared actual bug259_Annotation1 class Class1() extends super.Class1(){}
    shared class Class2Sub(){}

    shared interface Interface2Sub{}
}

@test
shared void bug259(){
    bug259DeclaredAttributes();
    bug259Attributes();
    bug259DeclaredMethods();
    bug259Methods();
    bug259DeclaredClasses();
    bug259Classes();
    bug259DeclaredInterfaces();
    bug259Interfaces();
}

void bug259DeclaredAttributes(){
    value instance = Bug259();
    value klass = `Bug259`;
    
    value attrs1 = klass.getDeclaredAttributes<Bug259,Integer,Nothing>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(attrs1.size == 1, exists attrs1First = attrs1.first, attrs1First.declaration.name == "attribute1");

    value attrs2 = klass.getDeclaredAttributes<Bug259,Integer,Nothing>(`SharedAnnotation`);
    assert(attrs2.size == 2, 
           attrs2.any((Attribute<Bug259,Integer,Nothing> a) => a.declaration.name == "attribute1"),
           attrs2.any((Attribute<Bug259,Integer,Nothing> a) => a.declaration.name == "attribute2"));

    value attrs3 = klass.getDeclaredAttributes<Bug259,Integer,Integer>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(attrs3.empty);

    value attrs4 = klass.getDeclaredAttributes<Bug259,String,String>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(attrs4.size == 1, exists attrs4First = attrs4.first, attrs4First.declaration.name == "attribute4");
    
    value attrs5 = klass.getDeclaredAttributes<Bug259,String,String>(`SharedAnnotation`);
    assert(attrs5.size == 2, 
           attrs5.any((Attribute<Bug259,String,String> a) => a.declaration.name == "attribute4"),
           attrs5.any((Attribute<Bug259,String,String> a) => a.declaration.name == "attribute5"));
    
    value attrs6 = klass.getDeclaredAttributes<Bug259,String,Nothing>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(attrs6.size == 1, exists attrs6First = attrs6.first, attrs6First.declaration.name == "attribute4");
}

void bug259Attributes(){
    value instance = Bug259Sub();
    value klass = `Bug259Sub`;
    
    value attrs1 = klass.getAttributes<Bug259Sub,Integer,Nothing>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(attrs1.size == 1, exists attrs1First = attrs1.first, attrs1First.declaration.name == "attribute1",
           attrs1First.declaration.container.name == "Bug259Sub");
    
    value attrs2 = klass.getAttributes<Bug259Sub,Integer,Nothing>(`SharedAnnotation`);
    assert(attrs2.size == 4, 
           attrs2.any((Attribute<Bug259Sub,Integer,Nothing> a) => a.declaration.name == "hash"),
           attrs2.any((Attribute<Bug259Sub,Integer,Nothing> a) => a.declaration.name == "attribute1"),
           attrs2.any((Attribute<Bug259Sub,Integer,Nothing> a) => a.declaration.name == "attribute2Sub"),
           attrs2.any((Attribute<Bug259Sub,Integer,Nothing> a) => a.declaration.name == "attribute2"));
}

void bug259DeclaredMethods(){
    value instance = Bug259();
    value klass = `Bug259`;
    
    value methods1 = klass.getDeclaredMethods<Bug259,Integer,[]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(methods1.size == 1, exists methods1First = methods1.first, methods1First.declaration.name == "method1");
    
    value methods2 = klass.getDeclaredMethods<Bug259,Integer,[]>(`SharedAnnotation`);
    assert(methods2.size == 2, 
           methods2.any((Method<Bug259,Integer,[]> m) => m.declaration.name == "method1"),
           methods2.any((Method<Bug259,Integer,[]> m) => m.declaration.name == "method2"));
    
    value methods3 = klass.getDeclaredMethods<Bug259,Integer,[Integer]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(methods3.empty);
}

void bug259Methods(){
    value instance = Bug259Sub();
    value klass = `Bug259Sub`;
    
    value methods1 = klass.getMethods<Bug259Sub,Integer,[]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(methods1.size == 1, exists methods1First = methods1.first, methods1First.declaration.name == "method1",
           methods1First.declaration.container.name == "Bug259Sub");
    
    value methods2 = klass.getMethods<Bug259Sub,Integer,[]>(`SharedAnnotation`);
    assert(methods2.size == 3, 
           methods2.any((Method<Bug259Sub,Integer,[]> m) => m.declaration.name == "method1"),
           methods2.any((Method<Bug259Sub,Integer,[]> m) => m.declaration.name == "method2Sub"),
           methods2.any((Method<Bug259Sub,Integer,[]> m) => m.declaration.name == "method2"));
    
}

void bug259DeclaredClasses(){
    value instance = Bug259();
    value klass = `Bug259`;
    
    value classes1 = klass.getDeclaredClasses<Bug259,Object,[]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(classes1.size == 1, exists classes1First = classes1.first, classes1First.declaration.name == "Class1");
    
    value classes2 = klass.getDeclaredClasses<Bug259,Object,[]>(`SharedAnnotation`);
    assert(classes2.size == 2, 
           classes2.any((MemberClass<Bug259,Object,[]> c) => c.declaration.name == "Class1"),
           classes2.any((MemberClass<Bug259,Object,[]> c) => c.declaration.name == "Class2"));
    
    value classes3 = klass.getDeclaredClasses<Bug259,Object,[Integer]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(classes3.empty);
}

void bug259Classes(){
    value instance = Bug259Sub();
    value klass = `Bug259Sub`;
    
    value classes1 = klass.getClasses<Bug259Sub,Object,[]>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(classes1.size == 1, exists classes1First = classes1.first, classes1First.declaration.name == "Class1",
           classes1First.declaration.container.name == "Bug259Sub");
    
    value classes2 = klass.getClasses<Bug259Sub,Object,[]>(`SharedAnnotation`);
    assert(classes2.size == 3, 
           classes2.any((MemberClass<Bug259Sub,Object,[]> c) => c.declaration.name == "Class1"),
           classes2.any((MemberClass<Bug259Sub,Object,[]> c) => c.declaration.name == "Class2Sub"),
           classes2.any((MemberClass<Bug259Sub,Object,[]> c) => c.declaration.name == "Class2"));
}

void bug259DeclaredInterfaces(){
    value instance = Bug259();
    value klass = `Bug259`;
    
    value interfaces1 = klass.getDeclaredInterfaces<Bug259,Object>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(interfaces1.size == 1, exists interfaces1First = interfaces1.first, interfaces1First.declaration.name == "Interface1");
    
    value interfaces2 = klass.getDeclaredInterfaces<Bug259,Object>(`SharedAnnotation`);
    assert(interfaces2.size == 2, 
           interfaces2.any((MemberInterface<Bug259,Object> i) => i.declaration.name == "Interface1"),
           interfaces2.any((MemberInterface<Bug259,Object> i) => i.declaration.name == "Interface2"));
    
    value interfaces3 = klass.getDeclaredInterfaces<Bug259,Bug259>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(interfaces3.empty);
}

void bug259Interfaces(){
    value instance = Bug259Sub();
    value klass = `Bug259Sub`;
    
    value interfaces1 = klass.getInterfaces<Bug259Sub,Object>(`SharedAnnotation`, `Bug259_Annotation1`);
    assert(interfaces1.size == 1, exists interfaces1First = interfaces1.first, interfaces1First.declaration.name == "Interface1",
           interfaces1First.declaration.container.name == "Bug259");
    
    value interfaces2 = klass.getInterfaces<Bug259Sub,Object>(`SharedAnnotation`);
    assert(interfaces2.size == 3, 
           interfaces2.any((MemberInterface<Bug259Sub,Object> i) => i.declaration.name == "Interface1"),
           interfaces2.any((MemberInterface<Bug259Sub,Object> i) => i.declaration.name == "Interface2Sub"),
           interfaces2.any((MemberInterface<Bug259Sub,Object> i) => i.declaration.name == "Interface2"));
}

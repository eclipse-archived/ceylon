import ceylon.language.meta.declaration { 
    Declaration, 
    ClassDeclaration, 
    ValueDeclaration, 
    FunctionDeclaration, 
    AnnotatedDeclaration 
}
import ceylon.language.meta { annotations }

final annotation class SeeThese(shared Declaration* declarations) satisfies Annotation<SeeThese> {}
annotation SeeThese seethese(Declaration* declarations) => SeeThese(*declarations);

final annotation class Meta(shared actual String string) satisfies SequencedAnnotation<Meta> {}
annotation Meta table(String name, String schema) { return Meta(name); }
annotation Meta persistent(String column, ClassDeclaration type, Boolean update) { return Meta(column); }

final annotation class An() satisfies OptionalAnnotation<An,AnnotatedDeclaration> {}
annotation An an() => An();

final annotation class Fun() satisfies OptionalAnnotation<Fun,FunctionDeclaration> {}
annotation Fun fun() => Fun();

final annotation class Att() satisfies OptionalAnnotation<Att,ValueDeclaration> {}
annotation Att att() => Att();

"A class"
by ("Gavin King",
    "Emmanuel Bernard")
class ToplevelClass() {}

final annotation class TypeDescription(String desc) 
    satisfies OptionalAnnotation<TypeDescription,Annotated> {}

final annotation class SequencedDescription(String desc) 
    satisfies SequencedAnnotation<SequencedDescription,Annotated> {}

class TrimmedString() {}
    
class Annotations() {
    
    void print("the thing to print" String text) {}
    
    deprecated void noop() {}
    
    "A class"
    by ("Gavin King",
        "Emmanuel Bernard")
    class LocalClass() {}
    
    seethese (`class ToplevelClass`, `class Annotations`)
    void accept(LocalClass c) {}
    
    table { name = "people"; 
            schema = "my"; }
    class Person() {
        
        persistent { column = "fullName";
                     update = true;
                     type = `class TrimmedString`; }
        shared String name = "Gavin King";
        
    }
    
    @error print ("hello") class Broken() {}
    
    /*void getMeOutOfTheInitialiserSection(){

        Class<Annotations,[]> at = `Annotations`;
        Member<Annotations,Class<TypeDescription,[String]>> tdt = `TypeDescription`;
        Member<Annotations,Class<SequencedDescription,[String]>> sdt = `SequencedDescription`;

        TypeDescription? d = annotations<TypeDescription,TypeDescription?,Annotated>(tdt(this), at.declaration);
        SequencedDescription[] ds = annotations<SequencedDescription,SequencedDescription[],Annotated>(sdt(this), at.declaration);
    }*/
    
}

@error fun fun fun String emptyStringFun0() => "";
an fun String emptyStringFun1() => "";
@error att String emptyStringFun2() => "";
@error fun String emptyStringAtt1 => "";
an att String emptyStringAtt2 => "";
an fun String? emptyOptionalStringFun() => null;
att String? emptyOptionalStringAtt => null;
an fun String? emptyStringFunWithParam(String s) => s;

An? fan = annotations(`An`, `function emptyStringFun1`);
An? van = annotations(`An`, `value emptyStringAtt2`);
Fun? ffun = annotations(`Fun`, `function emptyStringFun1`);
Att? vatt = annotations(`Att`, `value emptyStringAtt2`);

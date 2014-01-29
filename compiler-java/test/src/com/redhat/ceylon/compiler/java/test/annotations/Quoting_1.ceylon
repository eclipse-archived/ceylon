import ceylon.language.meta.declaration { ClassOrInterfaceDeclaration }

 @noanno
shared interface Cloneable<out Clone> of Clone
        given Clone satisfies Cloneable<Clone> {
    shared formal Clone clone;
}

annotation final class Quoting(
    shared actual String string,
    shared String toString,
    shared actual Integer hash,
    shared Integer hashCode) 
    satisfies SequencedAnnotation<Quoting, ClassOrInterfaceDeclaration>
        & Cloneable<Quoting> {

    shared actual Quoting clone => nothing;
}

annotation Quoting quoting() => Quoting("", "", -1, 0);

annotation Quoting quoting2(
String string,
String toString,
Integer hash,
Integer hashCode) => Quoting(string, toString, hash, hashCode);

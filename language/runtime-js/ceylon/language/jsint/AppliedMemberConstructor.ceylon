import ceylon.language.meta.declaration {
  Module, Declaration
}
import ceylon.language.meta.model {
  MemberClassConstructor, MemberClass, Constructor,
  ClosedType=Type
}

shared native class AppliedMemberConstructor<Container=Nothing,out Type=Anything, in Arguments=Nothing>
    (shared Module containingModule, Anything tipo)
    satisfies MemberClassConstructor<Container,Type,Arguments>
    given Arguments satisfies Anything[] {
  shared actual native MemberClass<Container,Type,Nothing> container;
  //Declared
  shared actual native Declaration declaration;
  //Qualified
  shared actual native Constructor<Type,Arguments> bind(Object container);
  //Functional
  shared actual native ClosedType<Anything>[] parameterTypes;
}

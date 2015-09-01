import ceylon.language.meta.declaration{
  ValueConstructorDeclaration
}
import ceylon.language.meta.model {
  MemberClassValueConstructor, MemberClass, ClassModel, ValueConstructor
}

shared native class AppliedMemberClassValueConstructor<in Container=Nothing, out Type=Object, in Set=Nothing>()
    satisfies MemberClassValueConstructor<Container,Type,Set> {
//        satisfies ValueModel<Type, Set> & Qualified<ValueConstructor<Type, Set>, Container> {
    
  shared native actual ValueConstructorDeclaration declaration;
  shared native actual MemberClass<Container, Type> type;
  shared native actual ClassModel<Type> container;
  //throws(`class StorageException`,
  //    "If this attribute is not stored at runtime, for example if it is neither shared nor captured.")
  shared native actual ValueConstructor<Type, Set> bind(Object container);
}

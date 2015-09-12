import ceylon.language.meta.declaration{
  ValueConstructorDeclaration
}
import ceylon.language.meta.model {
  ValueConstructor, Class
}

shared native class AppliedValueConstructor<out Type=Object,in Set=Nothing>()
        satisfies ValueConstructor<Type, Set> {
  shared native actual Type get();
  shared native actual void set(Set newValue);
  shared native actual void setIfAssignable(Anything newValue);
  shared native actual ValueConstructorDeclaration declaration;
  shared native actual Class<Type> type;
  shared native actual Class<Type>? container;

  shared native actual Boolean equals(Object other);
  shared native actual String string;
}


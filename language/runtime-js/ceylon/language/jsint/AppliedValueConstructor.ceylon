import ceylon.language.meta.declaration{
  ValueConstructorDeclaration
}
import ceylon.language.meta.model {
  ValueConstructor, Class
}

shared native class AppliedValueConstructor<out Type=Object>()
        satisfies ValueConstructor<Type> {
  shared native actual Type get();
  shared native actual void set(Nothing newValue);
  shared native actual void setIfAssignable(Anything newValue);
  shared native actual ValueConstructorDeclaration declaration;
  shared native actual Class<Type> type;
  shared native actual Class<Type>? container;

  shared native actual Boolean equals(Object other);
  shared native actual String string;
}

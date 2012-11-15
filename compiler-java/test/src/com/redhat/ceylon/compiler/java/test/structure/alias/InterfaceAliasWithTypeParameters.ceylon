shared interface InterfaceAliasWithTypeParameters_Foo<T>{}

shared interface InterfaceAliasWithTypeParameters<T> => InterfaceAliasWithTypeParameters_Foo<T>;
shared interface InterfaceAliasWithTypeParameters2 => InterfaceAliasWithTypeParameters_Foo<Integer>;

@nomodel
void interfaceAliasWithTypeParametersMethod(InterfaceAliasWithTypeParameters<Integer> a,
                                            InterfaceAliasWithTypeParameters2 b){
}